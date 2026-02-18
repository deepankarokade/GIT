package com.git.Professor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.git.Professor.Service.StudyMaterialService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.git.Professor.Entity.StudyMaterial;
import com.git.Admin.Service.CourseService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.List;
import com.git.Admin.Entity.Faculty;
import com.git.Admin.Repository.CourseRepository;

@Controller
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService service;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    // ===============================
    // MATERIAL CRUD (Base: /materials)
    // ===============================

    @GetMapping("/materials/upload")
    public String uploadPage() {
        return "professor/uploadMaterial";
    }

    @PostMapping("/materials/upload")
    public String uploadMaterial(@RequestParam("title") String title,
            @RequestParam(value = "subject", required = false) String subjectParam,
            @RequestParam(value = "category", defaultValue = "GENERAL") String category,
            @RequestParam(value = "course", required = false) String courseParam,
            @RequestParam("file") MultipartFile file,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        com.git.Admin.Entity.Faculty faculty = (com.git.Admin.Entity.Faculty) session.getAttribute("loggedProfessor");
        if (faculty == null) {
            return "redirect:/professor/login";
        }

        String facultyId = faculty.getUsername();
        // Prioritize faculty's assigned subject and department (course)
        String finalSubject = (faculty.getSubject() != null) ? faculty.getSubject() : subjectParam;
        String finalCourse = (faculty.getDepartment() != null && !faculty.getDepartment().isEmpty())
                ? faculty.getDepartment()
                : courseParam;

        // Resolve courseId if the finalCourse is a courseName
        java.util.Optional<com.git.Admin.Entity.Course> courseOpt = courseRepository.findByCourseName(finalCourse);
        if (courseOpt.isPresent()) {
            finalCourse = courseOpt.get().getCourseId();
        }

        service.uploadMaterial(title, finalSubject, category, finalCourse, facultyId, file);
        redirectAttributes.addFlashAttribute("message", "File Uploaded Successfully!");

        if ("SYLLABUS".equals(category)) {
            return "redirect:/professor/syllabus";
        }
        if ("VIDEO".equals(category)) {
            return "redirect:/professor/videos";
        }
        if ("NOTES".equals(category)) {
            return "redirect:/professor/pdfs";
        }
        if ("PRESENTATION".equals(category)) {
            return "redirect:/professor/presentations";
        }
        return "redirect:/materials/list";
    }

    @GetMapping("/materials/list")
    public String viewAllMaterials(Model model) {
        model.addAttribute("materials", service.getAllMaterials());
        model.addAttribute("courses", courseService.getAllCourses());
        return "professor/professor-StudyMaterial";
    }

    @GetMapping("/materials/delete/{id}")
    public String deleteMaterial(@PathVariable Long id, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        service.deleteMaterial(id);
        redirectAttributes.addFlashAttribute("message", "File Deleted Successfully!");

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/materials/list";
    }

    @GetMapping("/materials/download/{id}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long id) {
        return serveFile(id, true);
    }

    @GetMapping("/materials/view/{id}")
    public ResponseEntity<Resource> viewMaterial(@PathVariable Long id) {
        return serveFile(id, false);
    }

    private ResponseEntity<Resource> serveFile(Long id, boolean asAttachment) {
        Optional<StudyMaterial> materialOpt = service.getById(id);
        if (materialOpt.isPresent()) {
            StudyMaterial material = materialOpt.get();
            try {
                Path filePath = Paths.get(System.getProperty("user.dir")).resolve(material.getFilePath()).normalize();
                Resource resource = new UrlResource(java.util.Objects.requireNonNull(filePath.toUri()));

                if (resource.exists()) {
                    String disposition = asAttachment ? "attachment" : "inline";
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    disposition + "; filename=\"" + material.getFileName() + "\"")
                            .header(HttpHeaders.CONTENT_TYPE, material.getFileType())
                            .body(resource);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.notFound().build();
    }

    // ===============================
    // PROFESSOR DASHBOARD LINKS
    // (MATCHING YOUR HTML EXACTLY)
    // ===============================

    @GetMapping("/professor/pdfs")
    public String pdfPage(Model model, HttpSession session) {
        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");
        addProfessorAttributes(model, session);
        model.addAttribute("courses", courseService.getAllCourses());

        List<StudyMaterial> materials;
        if (faculty != null) {
            materials = service.getByFacultyAndCategory(faculty.getUsername(), "NOTES");
        } else {
            materials = service.getByCategory("NOTES");
        }
        model.addAttribute("materials", materials);
        return "professor/professor-notes-management";
    }

    @GetMapping("/professor/videos")
    public String videoPage(Model model, HttpSession session) {
        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");
        addProfessorAttributes(model, session);
        model.addAttribute("courses", courseService.getAllCourses());

        List<StudyMaterial> materials;
        if (faculty != null) {
            materials = service.getByFacultyAndCategory(faculty.getUsername(), "VIDEO");
        } else {
            materials = service.getByCategory("VIDEO");
        }
        model.addAttribute("materials", materials);
        return "professor/professor_video-managemnt";
    }

    @GetMapping("/professor/syllabus")
    public String syllabusPage(Model model, HttpSession session) {
        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");
        addProfessorAttributes(model, session);
        model.addAttribute("courses", courseService.getAllCourses());

        List<StudyMaterial> materials;
        if (faculty != null) {
            materials = service.getByFacultyAndCategory(faculty.getUsername(), "SYLLABUS");
        } else {
            materials = service.getByCategory("SYLLABUS");
        }
        model.addAttribute("materials", materials);
        return "professor/professor_syllabus-management";
    }

    @GetMapping("/professor/presentations")
    public String presentationPage(Model model, HttpSession session) {
        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");
        addProfessorAttributes(model, session);
        model.addAttribute("courses", courseService.getAllCourses());

        List<StudyMaterial> materials;
        if (faculty != null) {
            materials = service.getByFacultyAndCategory(faculty.getUsername(), "PRESENTATION");
        } else {
            materials = service.getByCategory("PRESENTATION");
        }
        model.addAttribute("materials", materials);
        return "professor/professor-presentation-management";
    }

    private void addProfessorAttributes(Model model, HttpSession session) {
        com.git.Admin.Entity.Faculty faculty = (com.git.Admin.Entity.Faculty) session.getAttribute("loggedProfessor");
        if (faculty != null) {
            model.addAttribute("professorSubject", faculty.getSubject());
            model.addAttribute("professorCourse", faculty.getDepartment());
        }
    }
}
