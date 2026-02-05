package com.git.Professor.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="demoquestions")
public class Demoquestions {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@ManyToOne
	@JoinColumn(name = "paper_id")
	private Demoquestionpaper demoquestionpaper;
	
	private String questionText;
    private String options;
    private String correctAnswer;
    private String matchPairs; 
    private String essay;
    private String shortAnswer;
    private String fillintheblanks;
    private String questionType;
    private int marks;
	public Demoquestions() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Demoquestions(long id, Demoquestionpaper demoquestionpaper, String questionText, String options,
			String correctAnswer, String matchPairs, String essay, String shortAnswer, String fillintheblanks,
			String questionType, int marks) {
		super();
		this.id = id;
		this.demoquestionpaper = demoquestionpaper;
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
		return "Demoquestions [id=" + id + ", demoquestionpaper=" + demoquestionpaper + ", questionText=" + questionText
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
	public Demoquestionpaper getDemoquestionpaper() {
		return demoquestionpaper;
	}
	public void setDemoquestionpaper(Demoquestionpaper demoquestionpaper) {
		this.demoquestionpaper = demoquestionpaper;
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
	
    
  
	
	
}
