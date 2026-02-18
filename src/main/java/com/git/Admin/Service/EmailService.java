package com.git.Admin.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendProfessorCredentials(String to, String username, String password) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Professor Login Credentials - GIT Forum");

            String htmlContent = String.format(
                    "<h3>Welcome to GIT Forum</h3>" +
                            "<p>Here are your login credentials:</p>" +
                            "<p><strong>Login ID:</strong> %s</p>" +
                            "<p><strong>Password:</strong> %s</p>" +
                            "<p>Please change your password after your first login.</p>",
                    username, password);

            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            System.out.println("Successfully sent professor credentials email to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send professor email to: " + to);
            e.printStackTrace();
        }
    }

    public void sendStudentCredentials(String to, String username, String password) {
        if (to == null || to.trim().isEmpty()) {
            System.err.println("Failed to send student credentials: Email address is empty.");
            return;
        }
        try {
            System.out.println("Attempting to send student credentials email to: " + to);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Student Login Credentials - GIT Forum");

            String htmlContent = String.format(
                    "<div style='font-family: Arial, sans-serif; color: #333;'>" +
                            "<h2>Welcome to GIT Forum!</h2>" +
                            "<p>Your student account has been approved.</p>" +
                            "<div style='background: #f4f4f4; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                            "<p><strong>Login ID (UID):</strong> %s</p>" +
                            "<p><strong>Password:</strong> %s</p>" +
                            "</div>" +
                            "<p><a href='" + baseUrl
                            + "/student/login' style='background-color: #2563eb; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Login Now</a></p>"
                            +
                            "<p>We recommend changing your password after your first login.</p>" +
                            "</div>",
                    username, password);

            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            System.out.println("Successfully sent student credentials email to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send student credentials email to: " + to);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendForgotPasswordEmail(String to, String name, String resetLink) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Reset Your Password - GIT Forum");

            String htmlContent = String.format(
                    "<div style='font-family: Arial, sans-serif; color: #333;'>" +
                            "<h3>Hello %s,</h3>" +
                            "<p>We received a request to reset your password. Click the button below to proceed:</p>" +
                            "<p><a href='%s' style='background-color: #135bec; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Reset Password</a></p>"
                            +
                            "<p>If you didn't request this, you can safely ignore this email.</p>" +
                            "</div>",
                    name, resetLink);

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send reset link email");
        }
    }

    public void sendRegistrationPaymentEmail(com.git.Student.Entity.Student student) {
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            System.err.println("Failed to send payment email: Email address is empty.");
            return;
        }
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(student.getEmail());
            helper.setSubject("Complete Your Registration - Payment Required");

            String encodedName = java.net.URLEncoder.encode(student.getFullName(), "UTF-8");
            String encodedEmail = java.net.URLEncoder.encode(student.getEmail(), "UTF-8");
            String encodedPhone = java.net.URLEncoder.encode(student.getContactNumber(), "UTF-8");
            String encodedUid = java.net.URLEncoder.encode(student.getUid(), "UTF-8");

            String paymentLink = String.format("%s/payment?name=%s&email=%s&phone=%s&uid=%s",
                    baseUrl, encodedName, encodedEmail, encodedPhone, encodedUid);

            String htmlContent = String.format(
                    "<div style='font-family: \"Lexend\", sans-serif; max-width: 600px; margin: auto; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden;'>"
                            +
                            "<div style='background: linear-gradient(135deg, #135bec 0%%, #6366f1 100%%); padding: 30px; text-align: center; color: white;'>"
                            +
                            "<h2 style='margin: 0; font-size: 24px;'>Welcome to ExamPortal</h2>" +
                            "<p style='margin: 10px 0 0; opacity: 0.9;'>One more step to complete your registration</p>"
                            +
                            "</div>" +
                            "<div style='padding: 30px; color: #1e293b; line-height: 1.6;'>" +
                            "<p>Hello <strong>%s</strong>,</p>" +
                            "<p>Thank you for registering. Your profile has been created successfully with UID: <strong>%s</strong>.</p>"
                            +
                            "<p>To activate your account and gain access to the examination portal, please complete the registration fee payment.</p>"
                            +
                            "<div style='text-align: center; margin: 30px 0;'>" +
                            "<a href='%s' style='background-color: #135bec; color: white; padding: 14px 28px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);'>Proceed to Payment</a>"
                            +
                            "</div>" +
                            "<p style='font-size: 14px; color: #64748b; background: #f8fafc; padding: 15px; border-radius: 8px; border-left: 4px solid #135bec;'>"
                            +
                            "<strong>Note:</strong> Once your payment is verified, you will receive another email with your login credentials."
                            +
                            "</p>" +
                            "<p style='margin-top: 20px;'>If the button above doesn't work, copy and paste this link into your browser:</p>"
                            +
                            "<p style='word-break: break-all; font-size: 12px; color: #135bec;'>%s</p>" +
                            "</div>" +
                            "<div style='padding: 20px; background: #f1f5f9; text-align: center; font-size: 12px; color: #94a3b8;'>"
                            +
                            "&copy; 2026 ExamPortal. All rights reserved." +
                            "</div>" +
                            "</div>",
                    student.getFullName(), student.getUid(), paymentLink, paymentLink);

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
            System.out.println("Successfully sent registration payment email to: " + student.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send registration payment email to: " + student.getEmail());
            e.printStackTrace();
        }
    }
}
