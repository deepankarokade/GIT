package com.git.Admin.Service;

import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.ExamSet;
import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.ExamSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExamSetService {

    @Autowired
    private ExamSetRepository examSetRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<ExamSet> getAllExamSets() {
        return examSetRepository.findAll();
    }

    public Optional<ExamSet> getExamSetById(Long id) {
        return examSetRepository.findById(id);
    }

    public ExamSet createExamSet(ExamSet examSet) {
        if (examSet.getSetName() == null || examSet.getSetName().trim().isEmpty()) {
            throw new RuntimeException("Set name is required");
        }

        examSet.setSetName(examSet.getSetName().trim());
        examSet.setSetId(generateSetUid());
        return examSetRepository.save(examSet);
    }

    private String generateSetUid() {
        return examSetRepository.findTopByOrderByIdDesc()
                .map(last -> {
                    String lastUid = last.getSetId();
                    if (lastUid != null && lastUid.startsWith("SET_")) {
                        try {
                            int lastNumber = Integer.parseInt(lastUid.substring(4));
                            return String.format("SET_%04d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            return String.format("SET_%04d", last.getId() + 1);
                        }
                    }
                    return String.format("SET_%04d", last.getId() + 1);
                })
                .orElse("SET_0001");
    }

    public ExamSet updateExamSet(Long id, ExamSet examSetDetails) {
        ExamSet examSet = examSetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExamSet not found with id: " + id));

        if (examSetDetails.getSetName() != null) {
            examSet.setSetName(examSetDetails.getSetName().trim());
        }

        return examSetRepository.save(examSet);
    }

    @Transactional
    public void deleteExamSet(Long id) {
        ExamSet examSet = examSetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExamSet not found with id: " + id));

        // Remove exam set from all courses (course_exam_sets join table)
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            if (course.getExamSets() != null && course.getExamSets().remove(examSet)) {
                courseRepository.save(course);
            }
        }

        examSetRepository.delete(examSet);
    }
}
