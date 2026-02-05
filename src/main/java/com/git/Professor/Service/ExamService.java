package com.git.Professor.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Professor.Entity.Exam;
import com.git.Professor.Repository.ExamRepository;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepo;

    public Exam saveExam(Exam exam) {
        return examRepo.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepo.findAll();
    }

    public Exam getExamById(Long id) {
        return examRepo.findById(id).orElse(null);
    }

    public void deleteExam(Long id) {
        examRepo.deleteById(id);
    }

    // ✅ UPCOMING EXAMS (future)
    public long countUpcomingExams() {
        LocalDate today = LocalDate.now();
        return examRepo.findAll()
                .stream()
                .filter(e -> e.getExamDate() != null)
                .filter(e -> e.getExamDate().isAfter(today))
                .count();
    }

    public long countTodayExams() {
        LocalDate today = LocalDate.now();
        return examRepo.findAll()
                .stream()
                .filter(e -> e.getExamDate() != null)
                .filter(e -> e.getExamDate().isEqual(today))
                .count();
    }

    public long getPublishedResultsCount() {
        return examRepo.findAll()
                .stream()
                .filter(e -> "Published".equalsIgnoreCase(e.getStatus()))
                .count();
    }

    public void updateExamStatus(Exam exam) {

        LocalDate today = LocalDate.now();

        if (exam.getExamDate() == null)
            return;

        if (exam.getExamDate().isAfter(today)) {
            exam.setStatus("UPCOMING");
        } else if (exam.getExamDate().isEqual(today)) {
            exam.setStatus("LIVE");
        } else {
            exam.setStatus("COMPLETED");
        }

        examRepo.save(exam);
    }

}
