package com.git.Admin.Controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.git.Admin.Entity.ManageExamType;
import com.git.Admin.Service.ManageExamTypeService;

@RestController
@RequestMapping("/admin/examtype")
public class ManageExamTypeController {

    @Autowired
    private ManageExamTypeService examTypeService;

    // GET all exam types
    @GetMapping
    public ResponseEntity<List<ManageExamType>> getAllExamTypes() {
        return ResponseEntity.ok(examTypeService.getAllExamTypes());
    }

    // GET exam type by database ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getExamTypeById(@PathVariable Long id) {
        try {
            ManageExamType examType = examTypeService.getExamTypeById(id);
            return ResponseEntity.ok(examType);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET exam type by examTypeId
    @GetMapping("/by-exam-type-id/{examTypeId}")
    public ResponseEntity<?> getExamTypeByExamTypeId(@PathVariable String examTypeId) {
        try {
            ManageExamType examType = examTypeService.getExamTypeByExamTypeId(examTypeId);
            return ResponseEntity.ok(examType);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET exam types by course ID
    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<List<ManageExamType>> getExamTypesByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(examTypeService.getExamTypesByCourseId(courseId));
    }

    // CREATE exam type (with optional course ID)
    @PostMapping
    public ResponseEntity<?> createExamType(
            @RequestBody ManageExamType examType,
            @RequestParam(required = false) Long courseId) {
        try {
            ManageExamType created = examTypeService.createExamTypeWithCourse(examType, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // UPDATE exam type (with optional course ID)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExamType(
            @PathVariable Long id,
            @RequestBody ManageExamType examType,
            @RequestParam(required = false) Long courseId) {
        try {
            ManageExamType updated = examTypeService.updateExamTypeWithCourse(id, examType, courseId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE exam type by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExamType(@PathVariable Long id) {
        try {
            examTypeService.deleteExamType(id);
            return ResponseEntity.ok("Exam Type deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE exam type by examTypeId
    @DeleteMapping("/by-exam-type-id/{examTypeId}")
    public ResponseEntity<?> deleteExamTypeByExamTypeId(@PathVariable String examTypeId) {
        try {
            examTypeService.deleteExamTypeByExamTypeId(examTypeId);
            return ResponseEntity.ok("Exam Type deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET count of exam types
    @GetMapping("/count")
    public ResponseEntity<Long> countExamTypes() {
        return ResponseEntity.ok(examTypeService.countExamTypes());
    }
}
