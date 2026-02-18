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

import com.git.Admin.DTO.CourseRequest;
import com.git.Admin.Entity.Course;
import com.git.Admin.Service.CourseService;
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

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.notFound().build();
    }

    // ADD New Course
    @PostMapping
    public ResponseEntity<?> addNewCourse(@RequestBody CourseRequest request) {
        try {
            Course savedCourse = courseService.addNewCourse(
                    request.getCourseName(),
                    request.getStudentClass(),
                    request.getBatchDescription(),
                    request.getCourseType(),
                    request.getQuestionTypes(),
                    request.getSubjectIds(),
                    request.getSetIds());
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
                    request.getSubjectIds(),
                    request.getSetIds());
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DELETE Course by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
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
