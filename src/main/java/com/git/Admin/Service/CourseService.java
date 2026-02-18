package com.git.Admin.Service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.Section;
import com.git.Admin.Entity.Subject;
import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.ExamSetRepository;
import com.git.Admin.Repository.ManageExamTypeRepository;
import com.git.Admin.Repository.SectionRepository;
import com.git.Admin.Repository.SubjectRepository;
import com.git.CourseType;
import com.git.QuestionType;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamSetRepository examSetRepository;

    @Autowired
    private ManageExamTypeRepository manageExamTypeRepository;

    @Autowired
    private SectionRepository sectionRepository;

    // Generate Course UID
    private String generateCourseUid() {
        return courseRepository.findTopByOrderByIdDesc()
                .map(lastCourse -> {
                    String lastUid = lastCourse.getCourseId();
                    if (lastUid != null && lastUid.startsWith("COU_")) {
                        try {
                            int lastNumber = Integer.parseInt(lastUid.substring(4));
                            return String.format("COU_%04d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            // Fallback if parsing fails
                            return String.format("COU_%04d", lastCourse.getId() + 1);
                        }
                    }
                    // Fallback if UID format is unexpected
                    return String.format("COU_%04d", lastCourse.getId() + 1);
                })
                .orElse("COU_0001"); // First course
    }

    // GET all Course
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByClass(String studentClass) {
        return courseRepository.findByStudentClass(studentClass);
    }

    public Course getCourseById(Long id) {
        if (id == null) {
            return null;
        }
        return courseRepository.findById(id).orElse(null);
    }

    // ADD new Course
    public Course addNewCourse(String courseName, String studentClass, String batchDescription,
            CourseType courseType, Set<QuestionType> questionTypes, List<Long> subjectIds, List<Long> setIds) {

        String courseId = generateCourseUid();

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

        if (setIds != null && !setIds.isEmpty()) {
            course.setExamSets(examSetRepository.findAllById(setIds));
        }

        return courseRepository.save(course);
    }

    // DELETE Course
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Not found"));

        // Remove course reference from all sections
        List<Section> sections = sectionRepository.findByCourse(course);
        for (Section section : sections) {
            section.setCourse(null);
            sectionRepository.save(section);
        }

        // Delete related records first
        manageExamTypeRepository.deleteByCourse(course);

        courseRepository.delete(course);
    }

    // GET Total Courses Count
    public long totalCoursesCount() {
        return courseRepository.count();
    }

    public Course updateCourse(long id, String courseName, String courseId, String studentClass,
            String batchDescription, boolean active, CourseType courseType, Set<QuestionType> questionTypes,
            List<Long> subjectIds, List<Long> setIds) {
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

        if (setIds != null) {
            course.setExamSets(examSetRepository.findAllById(setIds));
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
