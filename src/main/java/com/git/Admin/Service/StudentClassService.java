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
        studentClass.setClassId(generateUniqueClassId(className));

        return studentClassRepository.save(studentClass);
    }

    private String generateUniqueClassId(String className) {
        // Generate ID like CLS-CLASSNAME (removing spaces and making uppercase)
        return "CLS-" + className.toUpperCase().replaceAll("\\s+", "");
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
        existingClass.setClassId(generateUniqueClassId(className));

        return studentClassRepository.save(existingClass);
    }
}
