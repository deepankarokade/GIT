package com.git.Professor.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.git.Professor.Entity.Demoquestionpaper;
import com.git.Professor.Entity.Demoquestions;
import com.git.Professor.Entity.Exam;
import com.git.Professor.Service.Demoquestionsservice;
import com.git.Professor.Service.Demoqustionpaperservice;
import com.git.Professor.Service.ExamService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/demoquestionpaper")
public class SetDemoquestionpapercontroller {
	@Autowired
	Demoqustionpaperservice ps;

	@Autowired
	Demoquestionsservice qs;

	@Autowired
	ExamService es;

	@GetMapping("/viewpaper")
	public String viewquestionform(Model model) {

		List<Exam> exams = es.getAllExams();
		model.addAttribute("exams", exams);

		return "professor/demoquestion_paper";
	}

	@PostMapping("/savedemoQuestionPaper")
	@Transactional
	public String saveQuestionPaper(
			@RequestParam("coursename") String coursename,
			@RequestParam("subject") String subject,
			@RequestParam("chapter") String chapter,
			@RequestParam("duration") int duration,
			@RequestParam("difficulty") String difficulty,
			@RequestParam("selectset") String selectset,
			@RequestParam("explanation") String explanation,
			@RequestParam(value = "questionPaperImage", required = false) MultipartFile questionPaperImage,
			@RequestParam(value = "attachment", required = false) MultipartFile attachment,
			HttpServletRequest request) throws IOException {

		// 1️⃣ Save Question Paper
		Demoquestionpaper qp = new Demoquestionpaper();
		qp.setCoursename(coursename);
		qp.setSubject(subject);
		qp.setChapter(chapter);
		qp.setDuration(duration);
		qp.setDifficulty(difficulty);
		qp.setSelectset(selectset);
		qp.setExplanation(explanation);

		if (questionPaperImage != null && !questionPaperImage.isEmpty()) {
			qp.setQuestionpaperphotoname(questionPaperImage.getOriginalFilename());
			qp.setQuestionpaperimage(questionPaperImage.getBytes());
		}

		if (attachment != null && !attachment.isEmpty()) {
			qp.setAttachmentname(attachment.getOriginalFilename());
			qp.setAttachment(attachment.getBytes());
		}

		ps.savequestionpaper(qp);

		// 2️⃣ Save Questions dynamically
		int index = 0;
		while (request.getParameter("questions[" + index + "].questionText") != null) {
			Demoquestions q = new Demoquestions();
			q.setDemoquestionpaper(qp);

			// Question text
			q.setQuestionText(request.getParameter("questions[" + index + "].questionText"));

			// Marks
			String marksStr = request.getParameter("questions[" + index + "].marks");
			q.setMarks(marksStr != null ? Integer.parseInt(marksStr) : 0);

			// Question type
			String type = request.getParameter("questions[" + index + "].questionType");

			// ✅ Store question type
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
				q.setCorrectAnswer(shortAnswer); // ✅ also store in correctAnswer
			} else if ("essay".equals(type)) {
				String essayAnswer = request.getParameter("questions[" + index + "].essay");
				q.setEssay(essayAnswer);
				q.setCorrectAnswer(essayAnswer); // ✅ store in correctAnswer column too
			}

			else if ("fill".equals(type)) {
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

			qs.savequestions(q); // save each question
			index++;
		}

		return "redirect:/questionPaperList"; // redirect after save
	}

}
