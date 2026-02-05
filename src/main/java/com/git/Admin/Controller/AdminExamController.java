package com.git.Admin.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.git.Professor.Service.ExamService;

@RestController
@RequestMapping("/admin/exams")
public class AdminExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/count/active")
    public long getActiveExamsCount() {
        return examService.countTodayExams();
    }

    @GetMapping("/count/total")
    public long getTotalExamsCount() {
        return (long) examService.getAllExams().size();
    }

    @GetMapping("/count/upcoming")
    public long getUpcomingExamsCount() {
        return examService.countUpcomingExams();
    }
}
