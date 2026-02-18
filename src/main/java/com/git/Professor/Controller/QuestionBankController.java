package com.git.Professor.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.SectionRepository;
import com.git.Admin.Repository.SubjectRepository;
import com.git.Professor.Entity.DifficultySet;
import com.git.Professor.Entity.QuestionBankQuestion;
import com.git.Professor.Repository.QuestionBankRepository;
import com.git.Professor.Entity.QuestionFormWrapper;

@Controller
@RequestMapping("/professor/question-bank")
public class QuestionBankController {

    @Autowired private CourseRepository courseRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private SectionRepository sectionRepository;
    @Autowired private QuestionBankRepository questionBankRepository;

    @GetMapping
    public String openQuestionBankPage(
            @RequestParam(required = false) String course,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String division,
            @RequestParam(required = false) String subject,
            Model model) {

        QuestionFormWrapper wrapper = new QuestionFormWrapper();
        QuestionBankQuestion q = new QuestionBankQuestion();
        q.setCourse(course);
        q.setClassName(className);
        q.setDivision(division);
        q.setSubject(subject);
        wrapper.getQuestions().add(q);

        model.addAttribute("wrapper", wrapper);
        model.addAttribute("courseList", courseRepository.findAll());
        model.addAttribute("subjectList", subjectRepository.findAll());
        model.addAttribute("divisionList", sectionRepository.findAll());

        return "professor/question-bank";
    }

    @PostMapping("/save")
    public String saveQuestion(@ModelAttribute QuestionFormWrapper wrapper, RedirectAttributes redirectAttributes) {
        try {
            if (wrapper.getQuestions() != null && !wrapper.getQuestions().isEmpty()) {
                for (QuestionBankQuestion question : wrapper.getQuestions()) {
                    if (question.getQuestionText() != null && !question.getQuestionText().trim().isEmpty()) {
                        questionBankRepository.save(question);
                    }
                }
                QuestionBankQuestion first = wrapper.getQuestions().get(0);
                redirectAttributes.addAttribute("course", first.getCourse());
                redirectAttributes.addAttribute("className", first.getClassName());
                redirectAttributes.addAttribute("division", first.getDivision());
                redirectAttributes.addAttribute("subject", first.getSubject());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "redirect:/professor/question-bank";
    }

    @GetMapping("/view-set-questionpaper")
    public String viewSetWiseQuestionPaper(
            @RequestParam(required = false, defaultValue = "") String course,
            @RequestParam(required = false, defaultValue = "") String className,
            @RequestParam(required = false, defaultValue = "") String division,
            @RequestParam(required = false, defaultValue = "") String subject,
            @RequestParam(required = false) String set,
            Model model) {

        List<QuestionBankQuestion> questions;

        // जर 'set' रिकामी असेल किंवा 'ALL' असेल तर सर्व सेट्स आणा
        if (set == null || set.isEmpty() || set.equalsIgnoreCase("ALL")) {
            questions = questionBankRepository.findAllSetsQuestions(course, className, division, subject);
            model.addAttribute("set", "ALL SETS");
        } else {
            DifficultySet difficultySet = DifficultySet.valueOf(set.toUpperCase());
            questions = questionBankRepository.findFilteredQuestions(course, className, division, subject, difficultySet);
            model.addAttribute("set", set);
        }

        if (!questions.isEmpty()) {
            QuestionBankQuestion firstQ = questions.get(0);
            if (course.isEmpty()) course = firstQ.getCourse();
            if (className.isEmpty()) className = firstQ.getClassName();
            if (division.isEmpty()) division = firstQ.getDivision();
            if (subject.isEmpty()) subject = firstQ.getSubject();
        }

        model.addAttribute("course", course);
        model.addAttribute("className", className);
        model.addAttribute("division", division);
        model.addAttribute("subject", subject);
        model.addAttribute("questions", questions);

        return "professor/viewsetwise-questionpaper";
    }    @PostMapping("/generate-paper")
    public String generateFinalPaper(@RequestParam("questionIds") String ids, Model model) {
        // String IDs la List madhe convert kara
        List<Long> idList = java.util.Arrays.stream(ids.split(","))
                                            .map(Long::parseLong)
                                            .collect(java.util.stream.Collectors.toList());

        // Selected questions fetch kara
        List<QuestionBankQuestion> selectedQuestions = (List<QuestionBankQuestion>) questionBankRepository.findAllById(idList);

        // Header sathi basic info (pahilya prashnavarun)
        if (!selectedQuestions.isEmpty()) {
            QuestionBankQuestion first = selectedQuestions.get(0);
            model.addAttribute("course", first.getCourse());
            model.addAttribute("subject", first.getSubject());
            model.addAttribute("className", first.getClassName());
        }

        model.addAttribute("questions", selectedQuestions);
        return "professor/final-paper-print"; // Navin HTML file
    }
}