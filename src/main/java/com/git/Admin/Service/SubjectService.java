package com.git.Admin.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.Subject;
import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.SubjectRepository;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    // GET All Subjects
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Add a subject
    public Subject addSubject(String subjectName, String subjectCode, Subject subject) {
        if (subjectRepository.existsBySubjectName(subjectName)) {
            throw new RuntimeException("Subject Name Already Exists");
        }
        subject.setSubjectName(subjectName);
        subject.setSubjectCode(generateSubjectUid());
        return subjectRepository.save(subject);
    }

    private String generateSubjectUid() {
        return subjectRepository.findTopByOrderByIdDesc()
                .map(lastSubject -> {
                    String lastUid = lastSubject.getSubjectCode();
                    if (lastUid != null && lastUid.startsWith("SUB_")) {
                        try {
                            int lastNumber = Integer.parseInt(lastUid.substring(4));
                            return String.format("SUB_%04d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            return String.format("SUB_%04d", lastSubject.getId() + 1);
                        }
                    }
                    return String.format("SUB_%04d", lastSubject.getId() + 1);
                })
                .orElse("SUB_0001");
    }

    // Update subject
    public Subject updateSubject(long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.setSubjectName(subjectDetails.getSubjectName());

        return subjectRepository.save(subject);
    }

    // Delete a subject by string code
    public void deleteSubject(String subjectCode) {
        Subject subject = subjectRepository.findBySubjectCode(subjectCode)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subjectRepository.delete(subject);
    }

    // Delete a subject by ID
    @Transactional
    public void deleteSubjectById(long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Remove subject from all courses (course_subjects join table)
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            if (course.getSubjects() != null && course.getSubjects().remove(subject)) {
                courseRepository.save(course);
            }
        }

        subjectRepository.delete(subject);
    }

    // GET Total subjects count
    public Long totalSubjects() {
        return subjectRepository.count();
    }
}
