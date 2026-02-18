package com.git.Admin.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.Section;
import com.git.Admin.Entity.StudentClass;
import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.SectionRepository;
import com.git.Admin.Repository.StudentClassRepository;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private StudentClassRepository classRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public Section addSection(String sectionName, String sectionDescription, Long classId, Long courseId) {
        if (sectionRepository.existsBySectionName(sectionName)) {
            throw new RuntimeException("Section with name '" + sectionName + "' already exists");
        }

        Section section = new Section();
        section.setSectionName(sectionName);
        section.setSectionDescription(sectionDescription != null ? sectionDescription : "");
        section.setSectionId(generateSectionUid());

        if (classId != null) {
            StudentClass studentClass = classRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
            section.setStudentClass(studentClass);
        }

        if (courseId != null) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
            section.setCourse(course);
        }

        return sectionRepository.save(section);
    }

    private String generateSectionUid() {
        return sectionRepository.findTopByOrderByIdDesc()
                .map(lastSection -> {
                    String lastUid = lastSection.getSectionId();
                    if (lastUid != null && lastUid.startsWith("SEC_")) {
                        try {
                            int lastNumber = Integer.parseInt(lastUid.substring(4));
                            return String.format("SEC_%04d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            return String.format("SEC_%04d", lastSection.getId() + 1);
                        }
                    }
                    return String.format("SEC_%04d", lastSection.getId() + 1);
                })
                .orElse("SEC_0001");
    }

    public void deleteSection(Long id) {
        if (id != null) {
            sectionRepository.deleteById(id);
        }
    }

    public Section updateSection(Long id, String sectionName, String sectionDescription, Long classId, Long courseId) {
        if (id == null) {
            throw new RuntimeException("Section ID is required");
        }
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));

        if (!existingSection.getSectionName().equals(sectionName)
                && sectionRepository.existsBySectionName(sectionName)) {
            throw new RuntimeException("Section with name '" + sectionName + "' already exists");
        }

        existingSection.setSectionName(sectionName);
        existingSection.setSectionDescription(sectionDescription != null ? sectionDescription : "");

        if (classId != null) {
            StudentClass studentClass = classRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
            existingSection.setStudentClass(studentClass);
        }

        if (courseId != null) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
            existingSection.setCourse(course);
        }

        return sectionRepository.save(existingSection);
    }
}
