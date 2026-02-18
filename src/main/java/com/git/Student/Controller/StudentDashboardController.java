package com.git.Student.Controller;

import com.git.Payment.Service.PaymentService;
import com.git.Student.Entity.Student;
import com.git.Student.Repository.StudentRepository;
import com.git.Student.Service.StudentService;
import com.git.Professor.Service.CertificateService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller

public class StudentDashboardController {

    private final StudentService studentService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private StudentRepository studentRepository;

    public StudentDashboardController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Student Dashboard
    @GetMapping("/student/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");

        if (loggedStudent == null) {
            return "redirect:/student/login";
        }

        Student student = studentRepository.findById(loggedStudent.getId()).orElse(new Student());
        model.addAttribute("student", student);
        model.addAttribute("student", loggedStudent);
        model.addAttribute("fullName", student.getFullName());

        long certificatesCount = certificateService.countCertificatesByStudent(student.getUid());
        model.addAttribute("certificatesCount", certificatesCount);

        return "student/student-dashboard";
    }

    @GetMapping("/student/edit-profile")
    public String editProfile(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null)
            return "redirect:/student/login";

        model.addAttribute("student", loggedStudent); // prefill fields
        return "student/student-edit-profile";
    }

    @GetMapping("/student/support-ticket")
    public String supportTicket(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null)
            return "redirect:/student/login";
        model.addAttribute("student", loggedStudent);
        return "student/support-ticket";
    }

    @PostMapping("/student/edit-profile")
    public String updateProfile(@ModelAttribute Student formStudent,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Student sessionStudent = (Student) session.getAttribute("loggedStudent");

        if (sessionStudent == null) {
            return "redirect:/student/login";
        }

        // Copy updated values
        sessionStudent.setFullName(formStudent.getFullName());
        sessionStudent.setDob(formStudent.getDob());
        sessionStudent.setGender(formStudent.getGender());
        sessionStudent.setEmail(formStudent.getEmail());
        sessionStudent.setContactNumber(formStudent.getContactNumber());
        sessionStudent.setAddress(formStudent.getAddress());
        sessionStudent.setCity(formStudent.getCity());
        sessionStudent.setState(formStudent.getState());
        sessionStudent.setParentName(formStudent.getParentName());
        sessionStudent.setParentContact(formStudent.getParentContact());
        sessionStudent.setSchoolCollegeName(formStudent.getSchoolCollegeName());
        sessionStudent.setStudentClass(formStudent.getStudentClass());

        // Save in DB
        studentRepository.save(sessionStudent);

        // Update session also
        session.setAttribute("loggedStudent", sessionStudent);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");

        return "redirect:/student/dashboard";
    }

    // Logout
    @GetMapping("/student/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedStudent"); // remove user
        session.invalidate();
        return "redirect:/student/login";
    }

    // Show Change Password page (Dashboard)
    @GetMapping("/student/change-password-page")
    public String showDashboardChangePasswordPage(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null) {
            return "redirect:/student/login";
        }
        return "student/studentdashboardchange-password";
    }

    // Handle Change Password form submission
    @PostMapping("/student/change-password-form")
    public String dashboardChangePasswordForm(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null) {
            redirectAttributes.addFlashAttribute("error", "Session expired. Please login again.");
            return "redirect:/student/login";
        }

        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();
        confirmPassword = confirmPassword.trim();

        String regex = "^[a-zA-Z0-9]{8,15}$";

        if (!newPassword.matches(regex)) {
            model.addAttribute("error", "Password must be 8-15 alphanumeric.");
            return "student/studentdashboardchange-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "student/studentdashboardchange-password";
        }

        boolean success = studentService.changePasswordDashboard(
                loggedStudent.getUid(),
                oldPassword,
                newPassword);

        if (!success) {
            model.addAttribute("error", "Current password is incorrect.");
            return "student/studentdashboardchange-password";
        }

        redirectAttributes.addFlashAttribute("success", "Password updated successfully!");
        return "redirect:/student/dashboard"; // back to dashboard after success
    }

    // Student Payment History
    @GetMapping("/student/payments")
    public String viewPaymentHistory(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null) {
            return "redirect:/student/login";
        }

        model.addAttribute("student", loggedStudent);
        model.addAttribute("payments", paymentService.getPaymentsByStudentUid(loggedStudent.getUid()));

        return "student/invoice";
    }

}
