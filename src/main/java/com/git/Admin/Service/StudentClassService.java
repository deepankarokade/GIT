package com.git.Admin.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.git.Admin.Entity.StudentClass;
import com.git.Admin.Repository.StudentClassRepository;

@Service
public class StudentClassService {

    @Autowired
    private StudentClassRepository studentClassRepository;

    public List<StudentClass> getAllClasses() {
        return studentClassRepository.findAll();
    }

    public StudentClass addClass(String className, String classDescription) {
        if (studentClassRepository.existsByClassName(className)) {
            throw new RuntimeException("Class with name '" + className + "' already exists");
        }

        StudentClass studentClass = new StudentClass();
        studentClass.setClassName(className);
        studentClass.setClassDescription(classDescription != null ? classDescription : "");
        studentClass.setClassId(generateClassUid());

        return studentClassRepository.save(studentClass);
    }

    private String generateClassUid() {
        return studentClassRepository.findTopByOrderByIdDesc()
                .map(lastClass -> {
                    String lastUid = lastClass.getClassId();
                    if (lastUid != null && lastUid.startsWith("CLS_")) {
                        try {
                            int lastNumber = Integer.parseInt(lastUid.substring(4));
                            return String.format("CLS_%04d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            return String.format("CLS_%04d", lastClass.getId() + 1);
                        }
                    }
                    return String.format("CLS_%04d", lastClass.getId() + 1);
                })
                .orElse("CLS_0001");
    }

    public void deleteClass(Long id) {
        if (id != null) {
            studentClassRepository.deleteById(id);
        }
    }

    public StudentClass updateClass(Long id, String className, String classDescription) {
        if (id == null) {
            throw new RuntimeException("Class ID is required");
        }
        StudentClass existingClass = studentClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        if (!existingClass.getClassName().equals(className) && studentClassRepository.existsByClassName(className)) {
            throw new RuntimeException("Class with name '" + className + "' already exists");
        }

        existingClass.setClassName(className);
        existingClass.setClassDescription(classDescription != null ? classDescription : "");

        return studentClassRepository.save(existingClass);
    }
}
