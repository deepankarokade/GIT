package com.git.Professor.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.git.Professor.Entity.Exam;
import com.git.Professor.Service.ExamService;

@Controller
@RequestMapping("/professor/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/create")
    public String createExamForm(Model model) {
        model.addAttribute("exam", new Exam());
        return "professor/exam-create";
    }

    @GetMapping("/update/{id}")
    public String updateExamForm(@PathVariable Long id, Model model) {
        Exam exam = examService.getExamById(id);
        if (exam != null) {
            model.addAttribute("exam", exam);
            return "professor/exam-create";
        }
        return "redirect:/professor/dashboard";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveExam(@RequestBody Exam exam) {

        if (exam.getId() != null) {

            Exam existingExam = examService.getExamById(exam.getId());
            if (existingExam == null) {
                return ResponseEntity.badRequest().body("Exam not found");
            }

            existingExam.setTitle(exam.getTitle());
            existingExam.setCourseName(exam.getCourseName());
            existingExam.setBatch(exam.getBatch());
            existingExam.setSection(exam.getSection());
            existingExam.setExamType(exam.getExamType());
            existingExam.setExamDate(exam.getExamDate());
            existingExam.setExamVenueType(exam.getExamVenueType());
            existingExam.setAdditionalInformation(exam.getAdditionalInformation());
            existingExam.setExamSet(exam.getExamSet());
            existingExam.setDuration(exam.getDuration());
            existingExam.setTotalMarks(exam.getTotalMarks());

            // 🔥 status auto update
            examService.updateExamStatus(existingExam);

            return ResponseEntity.ok("Exam updated successfully");

        } else {
            // exam.setFacilityName("Demo Professor");

            examService.updateExamStatus(exam);

            return ResponseEntity.ok("Exam created successfully");
        }
    }

    // LIST EXAMS (AJAX)
    @GetMapping("/list")
    @ResponseBody
    public List<Exam> getAllExamsAjax() {
        return examService.getAllExams();
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.ok("Exam deleted successfully");
    }

    @PutMapping("/reschedule/{id}")
    @ResponseBody
    public ResponseEntity<?> rescheduleExam(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Exam exam = examService.getExamById(id);
        if (exam == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Exam not found");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String newDateStr = body.get("examDate");
            LocalDate newDate = LocalDate.parse(newDateStr);
            exam.setExamDate(newDate);
            examService.updateExamStatus(exam); // status auto update

            Map<String, String> response = new HashMap<>();
            response.put("message", "Exam rescheduled successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid date format");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
