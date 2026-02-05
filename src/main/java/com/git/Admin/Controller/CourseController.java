package com.git.Admin.Controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.git.Admin.Entity.Course;
import com.git.Admin.Service.CourseService;
import com.git.CourseType;
import com.git.QuestionType;

@RestController
@RequestMapping("/admin/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // GET All Courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/class/{className}")
    public ResponseEntity<List<Course>> getCoursesByClass(@PathVariable String className) {
        return ResponseEntity.ok(courseService.getCoursesByClass(className));
    }

    // ADD New Course
    @PostMapping
    public ResponseEntity<?> addNewCourse(@RequestBody CourseRequest request) {
        try {
            Course savedCourse = courseService.addNewCourse(
                    request.getCourseName(),
                    request.getCourseId(),
                    request.getStudentClass(),
                    request.getBatchDescription(),
                    request.getCourseType(),
                    request.getQuestionTypes(),
                    request.getSubjectIds());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // UPDATE Course
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseRequest request) {
        try {
            Course updatedCourse = courseService.updateCourse(
                    id,
                    request.getCourseName(),
                    request.getCourseId(),
                    request.getStudentClass(),
                    request.getBatchDescription(),
                    request.isActive(),
                    request.getCourseType(),
                    request.getQuestionTypes(),
                    request.getSubjectIds());
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DTO for adding course with subjects
    public static class CourseRequest {
        private String courseName;
        private String courseId;
        private String studentClass;
        private String batchDescription;
        private boolean active;
        private CourseType courseType;
        private Set<QuestionType> questionTypes;
        private List<Long> subjectIds;

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

        public List<Long> getSubjectIds() {
            return subjectIds;
        }

        public void setSubjectIds(List<Long> subjectIds) {
            this.subjectIds = subjectIds;
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
    }

    // DELETE Course by courseId
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/count/total")
    public long getTotalCourseCount() {
        return courseService.totalCoursesCount();
    }

    // UPDATE Question Types only
    @PutMapping("/{id}/question-types")
    public ResponseEntity<?> updateQuestionTypes(@PathVariable Long id, @RequestBody Set<QuestionType> questionTypes) {
        try {
            Course updatedCourse = courseService.updateQuestionTypes(id, questionTypes);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
