package com.git.Student.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.git.Professor.Entity.ExamPreparationForm;
import com.git.Professor.Service.ExamPreparationService;

@Controller
@RequestMapping("/student")
public class StudentExamPreparationController {

    @Autowired
    private ExamPreparationService service; // Professor chi service vaparli ahe

    @GetMapping("/exam-preparation")
    public String showStudentPage(
            @RequestParam(defaultValue = "0") int page, // Pagination sathi
            jakarta.servlet.http.HttpSession session,
            Model model) {

        com.git.Student.Entity.Student loggedStudent = (com.git.Student.Entity.Student) session
                .getAttribute("loggedStudent");
        if (loggedStudent == null) {
            return "redirect:/student/login";
        }
        model.addAttribute("student", loggedStudent);

        // 1. Dynamic Years Logic
        int currentYear = java.time.LocalDate.now().getYear();
        List<Integer> lastFiveYears = new java.util.ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lastFiveYears.add(currentYear - i);
        }
        model.addAttribute("years", lastFiveYears);

        // 2. Paging Logic (Service madhe Pageable use kara)
        // Jar service paged data det asel tar:
        Page<ExamPreparationForm> paperPage = service.findAllPaged(PageRequest.of(page, 5));
        model.addAttribute("papers", paperPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paperPage.getTotalPages());

        // Controller madhe hi line add kar
        List<String> dynamicSubjects = service.getDistinctSubjectNames();
        model.addAttribute("subjects", dynamicSubjects);
        return "student/student_exam_preparation";
    }

}