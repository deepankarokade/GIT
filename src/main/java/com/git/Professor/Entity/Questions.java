package com.git.Professor.Entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "questions")
public class Questions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "paper_id")
	private Questionpaper questionPaper;

	private String questionText;
	private String options;
	private String correctAnswer;
	private String matchPairs;
	private String essay;
	private String shortAnswer;
	private String fillintheblanks;
	private String questionType;
	private int marks;

	@Transient
	private List<String[]> parsedMatchPairs;

	public Questions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Questions(long id, Questionpaper questionPaper, String questionText, String options, String correctAnswer,
			String matchPairs, String essay, String shortAnswer, String fillintheblanks, String questionType,
			int marks) {
		super();
		this.id = id;
		this.questionPaper = questionPaper;
		this.questionText = questionText;
		this.options = options;
		this.correctAnswer = correctAnswer;
		this.matchPairs = matchPairs;
		this.essay = essay;
		this.shortAnswer = shortAnswer;
		this.fillintheblanks = fillintheblanks;
		this.questionType = questionType;
		this.marks = marks;
	}

	@Override
	public String toString() {
		return "Questions [id=" + id + ", questionPaper=" + questionPaper + ", questionText=" + questionText
				+ ", options=" + options + ", correctAnswer=" + correctAnswer + ", matchPairs=" + matchPairs
				+ ", essay=" + essay + ", shortAnswer=" + shortAnswer + ", fillintheblanks=" + fillintheblanks
				+ ", questionType=" + questionType + ", marks=" + marks + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Questionpaper getQuestionPaper() {
		return questionPaper;
	}

	public void setQuestionPaper(Questionpaper questionPaper) {
		this.questionPaper = questionPaper;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getMatchPairs() {
		return matchPairs;
	}

	public void setMatchPairs(String matchPairs) {
		this.matchPairs = matchPairs;
	}

	public String getEssay() {
		return essay;
	}

	public void setEssay(String essay) {
		this.essay = essay;
	}

	public String getShortAnswer() {
		return shortAnswer;
	}

	public void setShortAnswer(String shortAnswer) {
		this.shortAnswer = shortAnswer;
	}

	public String getFillintheblanks() {
		return fillintheblanks;
	}

	public void setFillintheblanks(String fillintheblanks) {
		this.fillintheblanks = fillintheblanks;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	public List<String[]> getParsedMatchPairs() {
		return parsedMatchPairs;
	}

	public void setParsedMatchPairs(List<String[]> parsedMatchPairs) {
		this.parsedMatchPairs = parsedMatchPairs;
	}

}
