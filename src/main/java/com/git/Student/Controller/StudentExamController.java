package com.git.Student.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.git.Admin.Service.AdminExamService;
import com.git.Professor.Entity.Exam;
import com.git.Professor.Entity.Questionpaper;
import com.git.Professor.Entity.Questions;
import com.git.Student.Service.StudentExamService;

@Controller
@RequestMapping("/student/exams")
public class StudentExamController {

    @Autowired
    private AdminExamService examService;

    @GetMapping("/exam/{examId}/papers")
    public String viewExamPapers(@PathVariable Long examId, Model model) {

        Exam exam = examService.getApprovedExamById(examId);

        model.addAttribute("exam", exam);
        model.addAttribute("papers", exam.getQuestionPapers());

        return "student/question-papers";
    }

    @Autowired
    private StudentExamService service;

    @GetMapping("/exam/{examId}/paper/{paperId}")
    public String viewQuestionPaper(@PathVariable Long examId,
            @PathVariable Long paperId,
            Model model) {

        Exam exam = examService.getApprovedExamById(examId);
        Questionpaper paper = service.getQuestionPaperById(paperId);

        if (!paper.getExam().getId().equals(exam.getId())) {
            throw new RuntimeException("Invalid paper for exam");
        }

        List<Questions> questions = paper.getLq();

        // existing match parsing (KEEP AS IS)
        questions.forEach(q -> {
            if ("match".equals(q.getQuestionType())
                    && q.getMatchPairs() != null
                    && !q.getMatchPairs().isEmpty()) {

                List<String[]> pairs = new ArrayList<>();
                String[] rawPairs = q.getMatchPairs().split("##");

                for (String p : rawPairs) {
                    String[] lr = p.split("\\|");
                    if (lr.length == 2) {
                        pairs.add(lr);
                    }
                }
                q.setParsedMatchPairs(pairs);
            }
        });

        boolean hasMcq = questions.stream().anyMatch(q -> "mcq".equals(q.getQuestionType()));
        boolean hasTf = questions.stream().anyMatch(q -> "tf".equals(q.getQuestionType()));
        boolean hasFill = questions.stream().anyMatch(q -> "fill".equals(q.getQuestionType()));
        boolean hasMatch = questions.stream().anyMatch(q -> "match".equals(q.getQuestionType()));
        boolean hasShort = questions.stream().anyMatch(q -> "short".equals(q.getQuestionType()));
        boolean hasLong = questions.stream().anyMatch(q -> "long".equals(q.getQuestionType()));
        boolean hasEssay = questions.stream().anyMatch(q -> "essay".equals(q.getQuestionType()));

        model.addAttribute("hasMcq", hasMcq);
        model.addAttribute("hasTf", hasTf);
        model.addAttribute("hasFill", hasFill);
        model.addAttribute("hasMatch", hasMatch);
        model.addAttribute("hasShort", hasShort);
        model.addAttribute("hasLong", hasLong);
        model.addAttribute("hasEssay", hasEssay);

        // existing attributes
        model.addAttribute("paper", paper);
        model.addAttribute("questions", questions);

        return "student/view-question-paper";
    }

    // @GetMapping("/exam/{examId}/paper/{paperId}/solve")
    // public String solveQuestionPaper(@PathVariable Long examId,
    // @PathVariable Long paperId,
    // Model model) {
    // Questionpaper paper = service.getQuestionPaperById(paperId);
    // model.addAttribute("paper", paper);
    // return "student/solve-question-paper"; // interactive template
    // }
}
