package com.git.Admin.DTO;

import java.util.List;
import java.util.Set;
import com.git.CourseType;
import com.git.QuestionType;

public class CourseRequest {
    private String courseName;
    private String courseId;
    private String studentClass;
    private String batchDescription;
    private boolean active;
    private CourseType courseType;
    private Set<QuestionType> questionTypes;
    private List<Long> subjectIds;
    private List<Long> setIds;

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

    public String getBatchDescription() {
        return batchDescription;
    }

    public void setBatchDescription(String batchDescription) {
        this.batchDescription = batchDescription;
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

    public List<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public List<Long> getSetIds() {
        return setIds;
    }

    public void setSetIds(List<Long> setIds) {
        this.setIds = setIds;
    }
}
