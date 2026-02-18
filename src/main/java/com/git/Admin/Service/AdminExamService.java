package com.git.Admin.Service;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Admin.Repository.AdminExamRepository;
import com.git.Professor.Entity.Exam;

@Service
public class AdminExamService {

    @Autowired
    private AdminExamRepository repo;

    // Pending exams list
    public List<Exam> getPendingExams() {
        return repo.findByStatus("PENDING");
    }

    // Approve exam
    public void approveExam(Long examId) {
        Exam exam = repo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        exam.setEnabled(true);

        LocalDate today = LocalDate.now();

        if (exam.getExamDate() == null) {
            exam.setStatus("UPCOMING");
        } 
        else if (exam.getExamDate().isAfter(today)) {
            exam.setStatus("UPCOMING");
        } 
        else if (exam.getExamDate().isEqual(today)) {
            exam.setStatus("LIVE");
        } 
        else {
            exam.setStatus("COMPLETED");
        }

        repo.save(exam);
    }

    
    public Exam getExamById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }
    
    public List<Exam> getApprovedExams() {
        // LIVE किंवा UPCOMING status असलेले exams
        return repo.findByStatusIn(List.of("LIVE", "UPCOMING"));
    }
    
    public Exam getApprovedExamById(Long examId) {

        Exam exam = repo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (!exam.isEnabled()) {
            throw new RuntimeException("Exam not approved");
        }

        if (!List.of("LIVE", "UPCOMING").contains(exam.getStatus())) {
            throw new RuntimeException("Exam not visible for students");
        }

        return exam;
    }

}
