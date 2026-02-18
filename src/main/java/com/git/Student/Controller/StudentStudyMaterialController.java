package com.git.Student.Controller;

import com.git.Student.Entity.Student;
import com.git.Professor.Entity.StudyMaterial;
import com.git.Professor.Service.StudyMaterialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student/materials")
public class StudentStudyMaterialController {

    @Autowired
    private StudyMaterialService materialService;

    @Autowired
    private com.git.Admin.Repository.CourseRepository courseRepository;

    @GetMapping("/syllabus")
    public String viewSyllabus(HttpSession session, Model model) {
        return getMaterialsByCategory(session, model, "SYLLABUS", "Syllabus");
    }

    @GetMapping("/video")
    public String viewVideos(HttpSession session, Model model) {
        return getMaterialsByCategory(session, model, "VIDEO", "Videos");
    }

    @GetMapping("/pdf")
    public String viewNotes(HttpSession session, Model model) {
        return getMaterialsByCategory(session, model, "NOTES", "PDF Notes");
    }

    @GetMapping("/presentation")
    public String viewPresentations(HttpSession session, Model model) {
        return getMaterialsByCategory(session, model, "PRESENTATION", "Presentations");
    }

    private String getMaterialsByCategory(HttpSession session, Model model, String category, String title) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) {
            return "redirect:/student/login";
        }

        String studentCourse = student.getSubjects(); // This stores courseName

        // Find the courseId for this courseName
        String courseToFilter = studentCourse;
        java.util.Optional<com.git.Admin.Entity.Course> courseOpt = courseRepository.findByCourseName(studentCourse);
        if (courseOpt.isPresent()) {
            courseToFilter = courseOpt.get().getCourseId();
        }

        List<StudyMaterial> materials = materialService.getByCategoryAndCourse(category, courseToFilter);

        model.addAttribute("student", student);
        model.addAttribute("materials", materials);
        model.addAttribute("categoryTitle", title);
        model.addAttribute("category", category);
        model.addAttribute("activeItem", category.toLowerCase());
        model.addAttribute("expandItem", "study");

        return "student/student-study_material";
    }
}
