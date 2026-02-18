package com.git.Professor.Controller;

import com.git.Professor.Entity.ProfessorLogin;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.git.Admin.Entity.Faculty;
import com.git.Admin.Repository.FacultyRepository;
import com.git.Admin.Service.FacultyService;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/faculty")
public class FacultyLoginController {

    private static final String RESET_SECRET = "RESET_SECRET_KEY_123";

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FacultyRepository fr;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ProfessorLogin login) {

        try {
            Faculty faculty = facultyService.loginFaculty(
                    login.getUsername(),
                    login.getPassword());

            return ResponseEntity.ok("Login successful. Welcome to Faculty Dashboard");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/viewforget")
    public String viewfacultyforget() {

        return "professor/facultyforgetpassword";

    }

    @GetMapping("/viewchangeforget")
    public String viewChangeForgetPassword(@RequestParam String token,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split("\\|");

            String email = parts[0];
            long expiry = Long.parseLong(parts[1]);
            String signature = parts[2];

            // ⏰ Expired
            if (System.currentTimeMillis() > expiry) {
                redirectAttributes.addFlashAttribute(
                        "error", "Reset link expired. Please login again.");
                return "redirect:/professor/login";
            }

            // 🔐 Invalid signature
            String data = email + "|" + expiry;
            String expectedSig = Base64.getEncoder()
                    .encodeToString((data + RESET_SECRET).getBytes());

            if (!signature.equals(expectedSig)) {
                redirectAttributes.addFlashAttribute(
                        "error", "Invalid reset link.");
                return "redirect:/professor/login";
            }

            model.addAttribute("email", email);
            return "professor/changeforgetpassword";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Reset link expired or invalid.");
            return "redirect:/professor/login";
        }
    }

    @PostMapping("/sendforgetmail")
    public String sendforgetmail(@RequestParam String email,
            RedirectAttributes redirectAttributes)
            throws MessagingException {

        Optional<Faculty> fc = facultyService.findfacultybymail(email);
        if (fc.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email not found");
            return "redirect:/faculty/viewforget";
        }

        // ⏱ expiry (15 minutes)
        long expiry = System.currentTimeMillis() + (12L * 60 * 60 * 1000);

        String data = email + "|" + expiry;
        String signature = Base64.getEncoder().encodeToString(
                (data + RESET_SECRET).getBytes());

        String token = Base64.getEncoder().encodeToString(
                (data + "|" + signature).getBytes());

        String resetLink = "http://localhost:8081/faculty/viewchangeforget?token=" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Reset Your Password");
        helper.setText("""
                <h3>Password Reset</h3>
                <p>Click below to reset your password:</p>
                <a href='%s'
                   style='padding:10px 16px;background:#2563eb;color:white;
                   text-decoration:none;border-radius:6px;font-weight:bold;'>
                   Change Password
                </a>
                <p>Link valid for 12 Hours.</p>
                """.formatted(resetLink), true);

        javaMailSender.send(message);

        redirectAttributes.addFlashAttribute(
                "info", "Password reset link sent");
        return "redirect:/professor/login";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/faculty/viewforget";
        }

        Optional<Faculty> facultyOpt = facultyService.findfacultybymail(email);
        if (facultyOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid request");
            return "redirect:/professor/login";
        }

        Faculty faculty = facultyOpt.get();
        faculty.setPassword(newPassword); // ⚠️ encode later
        fr.save(faculty);

        redirectAttributes.addFlashAttribute(
                "info", "Password updated successfully");

        return "redirect:/professor/login";
    }

}