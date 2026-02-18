package com.git.Admin.Controller;

import com.git.Admin.Entity.ExamSet;
import com.git.Admin.Service.ExamSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/exam-set")
public class ExamSetController {

    @Autowired
    private ExamSetService examSetService;

    @GetMapping
    public ResponseEntity<List<ExamSet>> getAllExamSets() {
        return ResponseEntity.ok(examSetService.getAllExamSets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExamSetById(@PathVariable Long id) {
        return examSetService.getExamSetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createExamSet(@RequestBody ExamSet examSet) {
        try {
            ExamSet createdSet = examSetService.createExamSet(examSet);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExamSet(@PathVariable Long id, @RequestBody ExamSet examSetDetails) {
        try {
            ExamSet updatedSet = examSetService.updateExamSet(id, examSetDetails);
            return ResponseEntity.ok(updatedSet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExamSet(@PathVariable Long id) {
        try {
            examSetService.deleteExamSet(id);
            return ResponseEntity.ok("Exam set deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
