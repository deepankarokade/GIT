package com.git.Professor.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.git.Professor.Service.ExamService;
import com.git.Admin.Service.FacultyService;
import com.git.Professor.Entity.Exam;
import com.git.Admin.Entity.Faculty;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ExamService examService;

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        // Get logged-in professor from session
        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");

        if (faculty == null) {
            // Not logged in → redirect to login page
            return "redirect:/professor/login";
        }

        List<Exam> exams = examService.getAllExams();
        model.addAttribute("exams", exams);
        model.addAttribute("totalStudents", 0);
        model.addAttribute("upcomingExams", examService.countUpcomingExams());
        model.addAttribute("liveExams", examService.countTodayExams());
        model.addAttribute("resultsPublished", examService.getPublishedResultsCount());

        // ✅ Set professor's actual name
        model.addAttribute("professorName", faculty.getFullName());

        return "professor/dashboard";
    }

    @GetMapping("/exams")
    public String viewAllExams(Model model) {
        List<Exam> exams = examService.getAllExams();
        model.addAttribute("exams", exams);
        return "professor/Examlist";
    }

    // Temporary test mapping for questionpaper
    @GetMapping("/questionpaper-test")
    public String questionpaperTest(@RequestParam(required = false) Long examId, Model model) {
        List<Exam> exams = examService.getAllExams();
        model.addAttribute("exams", exams);
        
        if (examId != null) {
            // Find exam by ID (simple implementation)
            Exam selectedExam = exams.stream()
                .filter(exam -> exam.getId().equals(examId))
                .findFirst()
                .orElse(null);
            model.addAttribute("selectedExam", selectedExam);
        }
        
        return "professor/question-paper";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "professor/login";
    }

    @PostMapping("/login")
    public String loginProfessor(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            // DB based login
            Faculty faculty = facultyService.loginFaculty(username, password);

            session.setAttribute("loggedProfessor", faculty);

            // Login successful → dashboard redirect
            return "redirect:/professor/dashboard?username=" + faculty.getFullName();
        } catch (RuntimeException e) {
            // Login failed → show error on login page
            model.addAttribute("error", e.getMessage());
            return "professor/login";
        }
    }

}
