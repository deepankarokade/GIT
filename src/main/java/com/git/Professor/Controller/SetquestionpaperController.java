package com.git.Professor.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.git.Professor.Entity.Exam;
import com.git.Professor.Entity.Questionpaper;
import com.git.Professor.Entity.Questions;
import com.git.Professor.Service.ExamService;
import com.git.Professor.Service.QuestionpaperService;
import com.git.Professor.Service.QuestionsService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/questionpaper")
public class SetquestionpaperController {

    @Autowired
    private QuestionpaperService qps;

    @Autowired
    private QuestionsService qs;

    @Autowired
    private ExamService es;

    // ================= TEST ENDPOINT =================
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "SetquestionpaperController is working!";
    }

    // ================= VIEW QUESTION PAPER FORM =================
    @GetMapping("/viewpaper")
    public String viewquestionform(
            @RequestParam(required = false) Long examId,
            Model model) {

        System.out.println("=== SetquestionpaperController.viewquestionform called ===");
        System.out.println("examId: " + examId);

        try {
            List<Exam> exams = es.getAllExams();
            model.addAttribute("exams", exams);

            if (examId != null) {
                Exam exam = es.getExamById(examId);
                model.addAttribute("selectedExam", exam);
                System.out.println("Selected exam: " + exam.getTitle());
            }

            System.out.println("Returning template: professor/question-paper");
            return "professor/question-paper";
        } catch (Exception e) {
            System.err.println("Error in viewquestionform: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    // ================= VIEW SET WISE PAPER =================
    @GetMapping("/exam/{examId}/view")
    public String viewSetWisePaper(@PathVariable Long examId, Model model) {

        Exam exam = es.getExamById(examId);
        List<Questionpaper> papers = qps.getPapersByExam(examId);

        model.addAttribute("exam", exam);
        model.addAttribute("papers", papers);

        return "professor/set-wise-paper";
    }

    @GetMapping("/view/{qid}")
    public String viewQuestionPaper(@PathVariable Long qid, Model model) {

        Questionpaper paper = qps.getQuestionPaperById(qid);
        List<Questions> questions = qs.getQuestionsByPaper(qid);

        for (Questions q : questions) {
            if ("match".equals(q.getQuestionType()) && q.getMatchPairs() != null) {

                List<String[]> pairs = new java.util.ArrayList<>();

                String[] rawPairs = q.getMatchPairs().split("##");
                for (String p : rawPairs) {
                    String[] lr = p.split("\\|");
                    if (lr.length == 2) {
                        pairs.add(lr); // [left, right]
                    }
                }

                q.setParsedMatchPairs(pairs);
            }
        }

        model.addAttribute("paper", paper);
        model.addAttribute("questions", questions);
        return "professor/view-paper";
    }

    // ================= SAVE QUESTION PAPER =================
    @PostMapping("/saveQuestionPaper")
    @Transactional
    public String saveQuestionPaper(
            @RequestParam("coursename") String coursename,
            @RequestParam("subject") String subject,
            @RequestParam("chapter") String chapter,
            @RequestParam("duration") int duration,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("selectset") String selectset,
            @RequestParam("examId") Long examId,
            @RequestParam("explanation") String explanation,
            @RequestParam(value = "questionPaperImage", required = false) MultipartFile questionPaperImage,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment,
            HttpServletRequest request) throws IOException {

        // ================= 1️⃣ CREATE QUESTION PAPER =================
        Questionpaper qp = new Questionpaper();
        qp.setCoursename(coursename);
        qp.setSubject(subject);
        qp.setChapter(chapter);
        qp.setDuration(duration);
        qp.setDifficulty(difficulty);
        qp.setExplanation(explanation);
        qp.setSelectset(selectset);

        // ================= SET EXAM (VERY IMPORTANT) =================
        Exam exam = es.getExamById(examId);
        qp.setExam(exam);

        if (questionPaperImage != null && !questionPaperImage.isEmpty()) {
            qp.setQuestionpaperphotoname(questionPaperImage.getOriginalFilename());
            qp.setQuestionpaperimage(questionPaperImage.getBytes());
        }

        if (attachment != null && !attachment.isEmpty()) {
            qp.setAttachmentname(attachment.getOriginalFilename());
            qp.setAttachment(attachment.getBytes());
        }

        // ================= SAVE QUESTION PAPER ONLY ONCE =================
        qps.savequestionpaper(qp);

        // ================= 2️⃣ SAVE QUESTIONS =================
        int index = 0;
        while (request.getParameter("questions[" + index + "].questionText") != null) {

            Questions q = new Questions();
            q.setQuestionPaper(qp); // FK mapping

            // Question Text
            q.setQuestionText(request.getParameter("questions[" + index + "].questionText"));

            // Marks
            String marksStr = request.getParameter("questions[" + index + "].marks");
            q.setMarks(marksStr != null ? Integer.parseInt(marksStr) : 0);

            // Question Type
            String type = request.getParameter("questions[" + index + "].questionType");
            q.setQuestionType(type);

            if ("mcq".equals(type)) {
                String[] options = request.getParameterValues("questions[" + index + "].options");
                if (options != null) {
                    q.setOptions(String.join("|", options));
                }
                q.setCorrectAnswer(request.getParameter("questions[" + index + "].correctAnswer"));

            } else if ("tf".equals(type)) {
                q.setCorrectAnswer(request.getParameter("questions[" + index + "].correctAnswer"));

            } else if ("long".equals(type)) {
                q.setCorrectAnswer(request.getParameter("questions[" + index + "].correctAnswer"));

            } else if ("short".equals(type)) {
                String shortAnswer = request.getParameter("questions[" + index + "].shortAnswer");
                q.setShortAnswer(shortAnswer);
                q.setCorrectAnswer(shortAnswer);

            } else if ("essay".equals(type)) {
                String essayAnswer = request.getParameter("questions[" + index + "].essay");
                q.setEssay(essayAnswer);
                q.setCorrectAnswer(essayAnswer);

            } else if ("fill".equals(type)) {
                String[] blanks = request.getParameterValues("questions[" + index + "].fillInTheBlanks");
                if (blanks != null) {
                    q.setFillintheblanks(String.join("|", blanks));
                }
                q.setCorrectAnswer(request.getParameter("questions[" + index + "].correctAnswer"));

            } else if ("match".equals(type)) {
                String[] lefts = request.getParameterValues("questions[" + index + "].leftItems");
                String[] rights = request.getParameterValues("questions[" + index + "].rightItems");
                if (lefts != null && rights != null && lefts.length == rights.length) {
                    StringBuilder pairs = new StringBuilder();
                    for (int i = 0; i < lefts.length; i++) {
                        if (pairs.length() > 0)
                            pairs.append("##");
                        pairs.append(lefts[i]).append("|").append(rights[i]);
                    }
                    q.setMatchPairs(pairs.toString());
                }
            }

            qs.savequestions(q);
            index++;
        }

        return "redirect:/professor/exams";
    }
}
