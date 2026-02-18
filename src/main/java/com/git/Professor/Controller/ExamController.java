package com.git.Professor.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.git.Admin.Entity.Faculty;
import com.git.Admin.Entity.Course;
import com.git.Admin.Entity.ManageExamType;
import com.git.Admin.Repository.CourseRepository;
import com.git.Admin.Repository.ManageExamTypeRepository;
import com.git.Professor.Entity.Exam;
import com.git.Professor.Service.ExamService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/professor/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ManageExamTypeRepository manageExamTypeRepository;

    // 1. परीक्षेचा फॉर्म उघडण्यासाठी
    @GetMapping("/create")
    public String createExamForm(HttpSession session, Model model) {
        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");

        if (faculty == null) return "redirect:/professor/login";

        model.addAttribute("professorName", faculty.getFullName());
        model.addAttribute("facultyId", faculty.getFacid());

        Exam exam = new Exam();
        exam.setProfessorName(faculty.getFullName());
        model.addAttribute("exam", exam);

        return "professor/exam-create";
    }

    // 2. अपडेट करण्यासाठी जुना डेटा फॉर्ममध्ये भरण्यासाठी
    @GetMapping("/update/{id}")
    public String updateExamForm(@PathVariable Long id, Model model) {
        Exam exam = examService.getExamById(id);
        if (exam != null) {
            model.addAttribute("exam", exam);
            return "professor/exam-create";
        }
        return "redirect:/professor/dashboard";
    }

    // 3. नवीन परीक्षा सेव्ह किंवा जुनी अपडेट करण्यासाठी
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveExam(@RequestBody Exam exam) {
        // वेळ तपासणी (Validation)
        if (exam.getStartTime() != null && exam.getEndTime() != null) {
            if (exam.getEndTime().isBefore(exam.getStartTime())) {
                return ResponseEntity.badRequest().body("End time must be after start time");
            }
        }

        if (exam.getId() != null) {
            // Update Existing Exam
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
            existingExam.setStartTime(exam.getStartTime());
            existingExam.setEndTime(exam.getEndTime());
            existingExam.setExamVenueType(exam.getExamVenueType());
            existingExam.setAdditionalInformation(exam.getAdditionalInformation());
            existingExam.setExamSet(exam.getExamSet());
            existingExam.setDuration(exam.getDuration());
            existingExam.setTotalMarks(exam.getTotalMarks());
            existingExam.setEnabled(exam.isEnabled()); // Checkbox status

            examService.updateExamStatus(existingExam);
            return ResponseEntity.ok("Exam updated successfully");

        } else {
            // Save New Exam
            exam.setExamCode(generateExamCode());
            exam.setStatus("PENDING");
            
            Exam savedExam = examService.saveExam(exam);
            return ResponseEntity.ok(String.valueOf(savedExam.getId()));
        }
    }

    // 4. सर्व परीक्षांची लिस्ट मिळवण्यासाठी (AJAX)
    @GetMapping("/list")
    @ResponseBody
    public List<Exam> getAllExamsAjax() {
        return examService.getAllExams();
    }

    // 5. परीक्षा डिलीट करण्यासाठी
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.ok("Exam deleted successfully");
    }

    // 6. रि-शेड्युल करण्यासाठी
    @PutMapping("/reschedule/{id}")
    @ResponseBody
    public ResponseEntity<?> rescheduleExam(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Exam exam = examService.getExamById(id);
        if (exam == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Exam not found"));
        }

        try {
            exam.setExamDate(LocalDate.parse(body.get("examDate")));
            exam.setStartTime(LocalTime.parse(body.get("startTime")));
            exam.setEndTime(LocalTime.parse(body.get("endTime")));

            examService.updateExamStatus(exam);
            return ResponseEntity.ok(Map.of("message", "Exam rescheduled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid format"));
        }
    }

 
    @GetMapping("/api/courses")
    @ResponseBody
    public List<Course> getAllCoursesForExam() {
        return courseRepository.findAll();
    }

    @GetMapping("/api/course-details/{courseId}")
    @ResponseBody
    public Map<String, Object> getCourseDetails(@PathVariable Long courseId) {
        Map<String, Object> response = new HashMap<>();
        
      
        List<ManageExamType> examTypes = manageExamTypeRepository.findByCourseId(courseId);
        response.put("examTypes", examTypes);
        
        return response;
    }

    // मदतनीस मेथड: एक्झाम कोड जनरेट करण्यासाठी
    private String generateExamCode() {
        return "EXM" + (1000 + new Random().nextInt(9000));
    }
}