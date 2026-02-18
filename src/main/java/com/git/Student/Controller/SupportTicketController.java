package com.git.Student.Controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.git.Admin.Service.AdminNotificationService;
import com.git.Student.Entity.SupportTicket;
import com.git.Student.Service.SupportTicketService;

import com.git.Student.Entity.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/support-ticket")
public class SupportTicketController {

    private final SupportTicketService supportTicketService;
    private final AdminNotificationService notificationService;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService,
            AdminNotificationService notificationService) {
        this.supportTicketService = supportTicketService;
        this.notificationService = notificationService;
    }

    // ================= VIEW SUPPORT TICKET FORM =================

    @GetMapping("/form")
    public String showSupportTicketForm(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) {
            return "redirect:/student/login";
        }
        model.addAttribute("student", student);
        return "student/support-ticket";
    }

    // ================= VIEW PREVIOUS SUPPORT TICKETS =================

    @GetMapping("/previous")
    public String showPreviousTickets(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) {
            return "redirect:/student/login";
        }

        java.util.List<SupportTicket> tickets = supportTicketService.getTicketsByStudent(student);
        model.addAttribute("tickets", tickets);
        model.addAttribute("student", student);
        return "student/student-previous-support-ticket";
    }

    // ================= SUBMIT SUPPORT TICKET =================

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String submitSupportTicket(
            @RequestParam("ticketType") String ticketType,
            @RequestParam("subject") String subject,
            @RequestParam("description") String description,
            @RequestParam("studentUid") String studentUid, // Accepted from form
            @RequestParam(value = "screenshot", required = false) MultipartFile screenshot,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null) {
            return "redirect:/student/login";
        }

        try {
            SupportTicket ticket = new SupportTicket();
            ticket.setTicketType(ticketType);
            ticket.setSubject(subject);
            ticket.setDescription(description);
            ticket.setStudent(loggedStudent); // Associate with student

            // ================= FILE UPLOAD =================
            if (screenshot != null && !screenshot.isEmpty()) {
                String uploadDir = System.getProperty("user.dir")
                        + "/uploads/screenshots/";

                File folder = new File(uploadDir);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String fileName = System.currentTimeMillis()
                        + "_" + screenshot.getOriginalFilename();

                File destination = new File(uploadDir + fileName);
                screenshot.transferTo(destination);

                ticket.setScreenshotPath("uploads/screenshots/" + fileName);
            }

            supportTicketService.saveTicket(ticket);

            // CREATE ADMIN NOTIFICATION
            String notificationMessage = "New support ticket from " + loggedStudent.getFullName() + " ("
                    + loggedStudent.getUid() + "): " + subject;
            notificationService.createNotification(notificationMessage, "SUPPORT_TICKET",
                    "/admin/dashboard/support-ticket");

            redirectAttributes.addFlashAttribute("message", "Support ticket submitted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute(
                    "error",
                    "File upload failed!");
        }

        return "redirect:/support-ticket/previous";
    }
}
