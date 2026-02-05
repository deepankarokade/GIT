package com.git.Admin.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.git.Admin.Entity.StudentClass;
import com.git.Admin.Service.StudentClassService;

@RestController
@RequestMapping("/admin/class")
public class StudentClassController {

    @Autowired
    private StudentClassService studentClassService;

    @GetMapping("/all")
    public ResponseEntity<List<StudentClass>> getAllClasses() {
        return ResponseEntity.ok(studentClassService.getAllClasses());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addClass(@RequestBody ClassRequest request) {
        try {
            StudentClass savedClass = studentClassService.addClass(request.getClassName(),
                    request.getClassDescription());
            return ResponseEntity.ok(savedClass);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        studentClassService.deleteClass(id);
        return ResponseEntity.ok("Class deleted successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Long id, @RequestBody ClassRequest request) {
        try {
            StudentClass updatedClass = studentClassService.updateClass(id, request.getClassName(),
                    request.getClassDescription());
            return ResponseEntity.ok(updatedClass);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class ClassRequest {
        private String className;
        private String classDescription;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassDescription() {
            return classDescription;
        }

        public void setClassDescription(String classDescription) {
            this.classDescription = classDescription;
        }
    }
}
