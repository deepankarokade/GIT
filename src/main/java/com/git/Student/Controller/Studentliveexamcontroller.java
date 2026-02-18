package com.git.Student.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.git.Professor.Entity.Exam;
import com.git.Professor.Repository.ExamRepository;
import com.git.Professor.Service.ExamService;
import com.git.Student.Entity.Student;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/student")
public class Studentliveexamcontroller {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRepository er;

    // 🔹 MANAGE EXAMS (ALL SECTIONS)
    @GetMapping("/manage-exam")
    public String manageExams(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null)
            return "redirect:/student/login";

        examService.refreshAllExamStatus();

        model.addAttribute("currentExams",
                er.findByStatusAndEnabledTrue("LIVE"));

        model.addAttribute("futureExams",
                er.findByStatusAndEnabledTrue("UPCOMING"));

        model.addAttribute("pastExams",
                er.findByStatusAndEnabledTrue("COMPLETED"));
        model.addAttribute("activeItem", "manage-exam");
        model.addAttribute("student", loggedStudent);
        return "student/manage-exam";
    }

    // 🔹 CURRENT (LIVE)
    @GetMapping("/exams/current")
    public String currentExams(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null)
            return "redirect:/student/login";

        examService.refreshAllExamStatus();

        List<Exam> exams = er.findByStatusAndEnabledTrue("LIVE");
        model.addAttribute("exams", exams);
        model.addAttribute("activeItem", "manage-exam-current");
        model.addAttribute("student", loggedStudent);
        return "student/current-exams";
    }

    // 🔹 FUTURE
    @GetMapping("/exams/future")
    public String futureExams(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null)
            return "redirect:/student/login";

        examService.refreshAllExamStatus();

        List<Exam> exams = er.findByStatusAndEnabledTrue("UPCOMING");
        model.addAttribute("exams", exams);
        model.addAttribute("activeItem", "manage-exam-future");
        model.addAttribute("student", loggedStudent);
        return "student/future-exams";
    }

    // 🔹 PAST
    @GetMapping("/exams/past")
    public String pastExams(HttpSession session, Model model) {
        Student loggedStudent = (Student) session.getAttribute("loggedStudent");
        if (loggedStudent == null)
            return "redirect:/student/login";

        examService.refreshAllExamStatus();

        List<Exam> exams = er.findByStatusAndEnabledTrue("COMPLETED");
        model.addAttribute("exams", exams);
        model.addAttribute("activeItem", "manage-exam-past");
        model.addAttribute("student", loggedStudent);
        return "student/past-exams";
    }
}
