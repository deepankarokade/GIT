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
}
