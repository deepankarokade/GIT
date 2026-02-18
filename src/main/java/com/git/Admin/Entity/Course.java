package com.git.Admin.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.git.CourseType;
import com.git.QuestionType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    @Column(unique = true)
    private String courseId;

    private String studentClass;

    private boolean active = true;

    @ManyToMany
    @JoinTable(name = "course_subjects", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    @ManyToMany
    @JoinTable(name = "course_exam_sets", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "exam_set_id"))
    private List<ExamSet> examSets;

    @Column(nullable = false)
    private String batchDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type", nullable = false)
    private CourseType courseType;

    @ElementCollection(targetClass = QuestionType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "course_question_types", joinColumns = @JoinColumn(name = "course_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private Set<QuestionType> questionTypes = new HashSet<>();

    public String getBatchDescription() {
        return batchDescription;
    }

    public void setBatchDescription(String batchDescription) {
        this.batchDescription = batchDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public Set<QuestionType> getQuestionTypes() {
        return questionTypes;
    }

    public void setQuestionTypes(Set<QuestionType> questionTypes) {
        this.questionTypes = questionTypes;
    }

    public List<ExamSet> getExamSets() {
        return examSets;
    }

    public void setExamSets(List<ExamSet> examSets) {
        this.examSets = examSets;
    }
}
