package com.git.Professor.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Professor.Entity.Questionpaper;
import com.git.Professor.Repository.QuestionpaperRepositry;

@Service
public class QuestionpaperService {

	@Autowired
	QuestionpaperRepositry qr;

	public void savequestionpaper(Questionpaper questionpaper) {

		qr.save(questionpaper);

	}

	public List<Questionpaper> getPapersByExam(Long examId) {
		return qr.findByExamId(examId);
	}

	public Questionpaper getQuestionPaperById(Long qid) {
		return qr.findById(qid).orElse(null);
	}

}
