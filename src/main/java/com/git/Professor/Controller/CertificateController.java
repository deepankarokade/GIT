package com.git.Professor.Controller;

import com.git.Admin.Entity.Faculty;
import com.git.Professor.Entity.Certificate;
import com.git.Professor.Service.CertificateService;
import com.git.Student.Entity.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    // PROFESSOR: Generate certificate
    @PostMapping("/professor/certificate/generate")
    @ResponseBody
    public ResponseEntity<?> generateCertificate(
            @RequestParam String studentUid,
            @RequestParam Long examId,
            HttpSession session) {

        Faculty loggedFaculty = (Faculty) session.getAttribute("loggedProfessor");
        if (loggedFaculty == null) {
            loggedFaculty = (Faculty) session.getAttribute("loggedFaculty");
        }

        if (loggedFaculty == null) {
            return ResponseEntity.status(401).body("Professor session not found");
        }

        try {
            Certificate certificate = certificateService.generateCertificate(studentUid, examId,
                    loggedFaculty.getUsername());
            return ResponseEntity.ok(certificate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ADMIN/PROFESSOR: View all certificates
    @GetMapping("/admin/certificates/manage")
    public String manageCertificates(Model model, HttpSession session) {
        // Check Admin or Professor session
        if (session.getAttribute("loggedAdmin") == null &&
                session.getAttribute("loggedProfessor") == null &&
                session.getAttribute("loggedFaculty") == null) {
            return "redirect:/";
        }

        List<Certificate> certificates = certificateService.getAllCertificates();
        model.addAttribute("certificates", certificates);
        return "admin/admin-manage-certificates";
    }

    // ADMIN/PROFESSOR: Edit certificate
    @PostMapping("/admin/certificate/update")
    @ResponseBody
    public ResponseEntity<?> updateCertificate(
            @RequestParam Long id,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
            HttpSession session) {

        if (session.getAttribute("loggedAdmin") == null &&
                session.getAttribute("loggedProfessor") == null &&
                session.getAttribute("loggedFaculty") == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            Certificate updated = certificateService.updateCertificate(id, grade, issueDate);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // STUDENT: View my certificates
    @GetMapping("/student/certificates")
    public String studentCertificates(Model model, HttpSession session) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null) {
            return "redirect:/student/login";
        }

        List<Certificate> certificates = certificateService.getCertificatesByStudent(loggedStudent.getUid());
        model.addAttribute("certificates", certificates);
        model.addAttribute("student", loggedStudent);
        return "student/student-certificates";
    }

    // View specific certificate (Final HTML view)
    @GetMapping("/certificate/view/{id}")
    public String viewCertificate(@PathVariable Long id, Model model) {
        Certificate certificate = certificateService.getCertificateById(id);
        model.addAttribute("certificate", certificate);
        return "certificate-view";
    }

    // ADMIN API: Get total certificate count
    @GetMapping("/admin/certificates/count/total")
    @ResponseBody
    public long getTotalCertificateCount() {
        return certificateService.countTotalCertificates();
    }
}
