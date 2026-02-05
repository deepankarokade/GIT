package com.git.Payment.Service;

import org.springframework.stereotype.Service;

import com.git.Admin.Service.EmailService;
import com.git.Payment.Entity.Payment;
import com.git.Payment.Repository.PaymentRepository;
import com.git.Student.Entity.Student;
import com.git.Student.Repository.StudentRepository;
import com.git.Student.enumactivity.ActivityStudent;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

@Service
public class PaymentService {
    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.webhook.secret:}")
    private String webhookSecret;

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    public PaymentService(PaymentRepository paymentRepository,
            StudentRepository studentRepository,
            EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
        this.emailService = emailService;
    }

    public Payment createPayment(Long amount, String currency) {
        Payment payment = new Payment();

        payment.setAmount(amount);
        payment.setCurrency(currency);

        return paymentRepository.save(payment);
    }

    // GET All Payments
    public List<Payment> getAllPayment() {
        return paymentRepository.findAll();
    }

    // Create Razorpay Order (with student details for approval flow)
    public Map<String, Object> createRazorpayOrder(Long amount, String currency,
            String studentName, String studentUid, String studentEmail)
            throws RazorpayException {

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStudentName(studentName);
        payment.setStudentUid(studentUid);
        payment.setStudentEmail(studentEmail);
        payment = paymentRepository.save(payment);

        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", "rcpt_" + payment.getId());

        Order order = razorpayClient.orders.create(orderRequest);

        payment.setRazorpayOrderId(order.get("id"));
        paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("paymentId", payment.getId());
        response.put("razorpayOrderId", order.get("id"));
        response.put("amount", payment.getAmount());
        response.put("currency", payment.getCurrency());
        response.put("key", keyId);

        return response;
    }

    // Approve Payment - Admin approves and credentials are sent to student
    public Payment approvePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Check if payment is SUCCESS (paid)
        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new RuntimeException("Cannot approve payment. Payment status is: " + payment.getStatus());
        }

        // Find the student by UID
        Student student = studentRepository.findByUid(payment.getStudentUid())
                .orElseThrow(() -> new RuntimeException("Student not found with UID: " + payment.getStudentUid()));

        // Generate password for student
        String generatedPassword = UUID.randomUUID().toString().substring(0, 10);
        student.setPassword(generatedPassword);

        // Activate student account
        student.setActivityStudent(ActivityStudent.ACTIVE);
        studentRepository.save(student);

        // Send credentials email
        try {
            emailService.sendStudentCredentials(
                    payment.getStudentEmail(),
                    student.getUid(),
                    generatedPassword);
        } catch (Exception e) {
            System.err.println("Failed to send student credentials email: " + e.getMessage());
        }

        // Update payment status to APPROVED
        payment.setStatus("APPROVED");
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return payment;
    }

    // Verify Payment
    public boolean verifyPaymentSignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey = new SecretKeySpec(
                    keySecret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");
            mac.init(secretKey);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            String generatedSignature = toHex(hash);

            return generatedSignature.equals(razorpaySignature);

        } catch (Exception e) {
            return false;
        }
    }

    // Mark SUCCESS
    public void markPaymentSuccess(
            String razorpayOrderId,
            String razorpayPaymentId) {

        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setStatus("SUCCESS");
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    // Mark FAILED
    public void markPaymentFailed(
            String razorpayOrderId) {
        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("FAILED");
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    /*
     * FOR ADMIN
     */

    // GET Payment by ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    // GET Payment by Order ID
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    // GET All Payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /*
     * WEBHOOK
     */

    // Verify Payment Webhook
    public boolean verifyWebhookSignature(
            String payload,
            String razorpaySignature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec keySpec = new SecretKeySpec(webhookSecret.getBytes("StandardCharsets.UTF_8"),
                    "HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String generatedSignature = toHex(hash);

            return generatedSignature.equals(razorpaySignature);
        } catch (Exception e) {
            return false;
        }
    }

    // Process Webhook Event
    public void processWebhookEvent(String payload) {
        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");

        if ("payment.captured".equals(eventType)) {
            JSONObject paymentEntity = event.getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String orderId = paymentEntity.getString("order_id");
            String paymentId = paymentEntity.getString("id");

            markPaymentSuccess(orderId, paymentId);
        }

        if ("payment.failed".equals(eventType)) {
            JSONObject paymentEntityJsonObject = event.getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String orderId = paymentEntityJsonObject.getString("order_id");
            markPaymentFailed(orderId);
        }
    }

    private String toHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
