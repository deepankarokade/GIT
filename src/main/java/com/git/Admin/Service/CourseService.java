package com.git.Admin.Service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.Subject;
import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.SubjectRepository;
import com.git.CourseType;
import com.git.QuestionType;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // GET all Course
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByClass(String studentClass) {
        return courseRepository.findByStudentClass(studentClass);
    }

    // ADD new Course
    public Course addNewCourse(String courseName, String courseId, String studentClass, String batchDescription,
            CourseType courseType, Set<QuestionType> questionTypes, List<Long> subjectIds) {

        if (courseRepository.existsByCourseId(courseId)) {
            throw new RuntimeException("CourseId Exists");
        }

        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseId(courseId);
        course.setStudentClass(studentClass);
        course.setBatchDescription(batchDescription);
        course.setCourseType(courseType);
        if (questionTypes != null) {
            course.setQuestionTypes(questionTypes);
        }

        if (subjectIds != null && !subjectIds.isEmpty()) {
            List<Subject> selectedSubjects = subjectRepository.findAllById(subjectIds);
            course.setSubjects(selectedSubjects);
        }

        return courseRepository.save(course);
    }

    // DELETE Course
    public void deleteCourse(String courseId) {
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course Not found"));

        courseRepository.delete(course);
    }

    // GET Total Courses Count
    public long totalCoursesCount() {
        return courseRepository.count();
    }

    public Course updateCourse(long id, String courseName, String courseId, String studentClass,
            String batchDescription, boolean active, CourseType courseType, Set<QuestionType> questionTypes,
            List<Long> subjectIds) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Not found"));

        // Check if name/id already exists in other courses
        if (!course.getCourseId().equals(courseId) && courseRepository.existsByCourseId(courseId)) {
            throw new RuntimeException("Course ID Already Exists");
        }

        course.setCourseName(courseName);
        course.setCourseId(courseId);
        course.setStudentClass(studentClass);
        course.setBatchDescription(batchDescription);
        course.setActive(active);
        course.setCourseType(courseType);
        if (questionTypes != null) {
            course.setQuestionTypes(questionTypes);
        }

        if (subjectIds != null) {
            List<Subject> selectedSubjects = subjectRepository.findAllById(subjectIds);
            course.setSubjects(selectedSubjects);
        }

        return courseRepository.save(course);
    }

    // UPDATE Question Types only
    public Course updateQuestionTypes(Long id, Set<QuestionType> questionTypes) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setQuestionTypes(questionTypes);
        return courseRepository.save(course);
    }
}
