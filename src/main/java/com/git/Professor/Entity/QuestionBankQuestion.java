package com.git.Professor.Entity;

import com.git.QuestionType;

import jakarta.persistence.*;

@Entity
@Table(name = "question_bank")
public class QuestionBankQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Context fields
    private String course;
    private String className;
    private String division;
    private String subject;
    // private String chapter;

    // Set A / B / C / D
    @Enumerated(EnumType.STRING)
    private DifficultySet difficultySet;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Lob
    @Column(nullable = false)
    private String questionText;

    // private String questionType; // MCQ / DESCRIPTIVE
    private Integer marks;

    // MCQ options (nullable for descriptive)
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer; // A / B / C / D

    @Lob
    private String matchPairs; // Serialized pairs for Match the Pairs

    // ---------------- Getters & Setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    // public String getChapter() {
    // return chapter;
    // }
    //
    // public void setChapter(String chapter) {
    // this.chapter = chapter;
    // }

    public DifficultySet getDifficultySet() {
        return difficultySet;
    }

    public void setDifficultySet(DifficultySet difficultySet) {
        this.difficultySet = difficultySet;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    // public String getQuestionType() {
    // return questionType;
    // }
    //
    // public void setQuestionType(String questionType) {
    // this.questionType = questionType;
    // }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
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

    private String fillInTheBlanks; // Serialized answers for Fill in the Blanks

    public String getFillInTheBlanks() {
        return fillInTheBlanks;
    }

    public void setFillInTheBlanks(String fillInTheBlanks) {
        this.fillInTheBlanks = fillInTheBlanks;
    }
}
