package com.git.Professor.Controller;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.git.Professor.Service.ExamService;
import com.git.Admin.Service.FacultyService;
import com.git.Professor.Entity.Exam;
import com.git.Professor.Entity.QuestionBankQuestion;
import com.git.Professor.Entity.Questions;
import com.git.Professor.Repository.QuestionBankRepository;
import com.git.Professor.Repository.QuestionRepositry;
import com.git.Admin.Entity.Faculty;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ExamService examService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");

        if (faculty == null) {
            return "redirect:/professor/login";
        }

        List<Exam> exams = examService.getAllExams();
        model.addAttribute("exams", exams);
        model.addAttribute("totalStudents", 0);
        model.addAttribute("upcomingExams", examService.countUpcomingExams());
        model.addAttribute("liveExams", examService.countTodayExams());
        model.addAttribute("resultsPublished", examService.getPublishedResultsCount());
        model.addAttribute("professorName", faculty.getFullName());
        model.addAttribute("facultyId", faculty.getFacid());

        return "professor/dashboard";
    }

    // ================= EXAMS =================
    @GetMapping("/exams")
    public String viewAllExams(Model model) {
        List<Exam> exams = examService.getAllExams();
        model.addAttribute("exams", exams);
        return "professor/Examlist";
    }

    // ================= LOGIN PAGE =================
    /*
     * @GetMapping("/login")
     * public String showLoginPage(Model model, HttpSession session) {
     * 
     * // popup flag read from session and send to model
     * if (session.getAttribute("showResetPopup") != null) {
     * model.addAttribute("showResetPopup", true);
     * }
     * 
     * return "professor/login";
     * }
     */

    @GetMapping("/login")
    public String showLoginPage() {
        return "professor/login";
    }

    // ================= LOGIN POST =================
    @PostMapping("/login")
    public String loginProfessor(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Faculty faculty = facultyService.loginFaculty(username, password);

            session.setAttribute("loggedProfessor", faculty);

            redirectAttributes.addFlashAttribute(
                    "loginSuccess",
                    "Login successful!");

            // ✅ ONLY FIRST TIME
            if (faculty.isFirstLogin()) {
                redirectAttributes.addFlashAttribute("showResetPopup", true);
                return "redirect:/professor/login";
            }

            return "redirect:/professor/dashboard";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/professor/login";
        }
    }

    // ================= SKIP RESET =================
    @GetMapping("/skip-reset")
    public String skipReset(HttpSession session) {

        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");

        if (faculty != null) {
            faculty.setFirstLogin(false);
            facultyService.registerFaculty(faculty); // save update
            session.setAttribute("loggedProfessor", faculty);
        }

        return "redirect:/professor/dashboard";
    }

    // ================= CHANGE PASSWORD PAGE =================
    @GetMapping("/change-password")
    public String showChangePassword(HttpSession session) {

        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");
        if (faculty == null) {
            return "redirect:/professor/login";
        }
        return "professor/change-password";
    }

    // ================= CHANGE PASSWORD POST =================
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) {

        Faculty faculty = (Faculty) session.getAttribute("loggedProfessor");

        if (faculty == null) {
            return "redirect:/professor/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirm password do not match");
            return "professor/change-password";
        }

        try {
            facultyService.changePassword(
                    faculty.getFacid(), // ✅ CORRECT ID
                    oldPassword,
                    newPassword);

            session.invalidate(); // force re-login
            model.addAttribute("success", "Password changed successfully. Please login again.");
            return "professor/login";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "professor/change-password";
        }
    }

    @GetMapping("/photo/{id}")
    public void getProfessorPhoto(
            @PathVariable Long id,
            HttpServletResponse response) {

        Faculty faculty = facultyService.getFacultyById(id);

        if (faculty != null && faculty.getPhoto() != null) {
            try {
                response.setContentType("image/jpeg"); // or image/png
                response.getOutputStream().write(faculty.getPhoto());
                response.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // ================= EDIT PROFILE PAGE =================
    /*
     * @GetMapping("/edit-profile")
     * public String editProfile(HttpSession session, Model model) {
     * 
     * Faculty professor =
     * (Faculty) session.getAttribute("loggedProfessor");
     * 
     * if (professor == null) {
     * return "redirect:/professor/login";
     * }
     * 
     * model.addAttribute("faculty", professor);
     * return "professor/edit-profile";
     * }
     */

    // ================= UPDATE PROFILE =================
    /*
     * @PostMapping("/edit-profile")
     * public String updateProfile(
     * Faculty facultyForm,
     * HttpSession session,
     * RedirectAttributes redirectAttributes) {
     * 
     * Faculty loggedFaculty = (Faculty) session.getAttribute("loggedProfessor");
     * 
     * if (loggedFaculty == null) {
     * return "redirect:/professor/login";
     * }
     * 
     * Faculty updatedFaculty =
     * facultyService.updateOwnProfile(
     * loggedFaculty.getFacid(),
     * facultyForm
     * );
     * 
     * // ✅ session refresh
     * session.setAttribute("loggedProfessor", updatedFaculty);
     * 
     * redirectAttributes.addFlashAttribute(
     * "success",
     * "Profile updated successfully"
     * );
     * 
     * return "redirect:/professor/dashboard";
     * }
     */

    // ================= EDIT PROFILE PAGE =================
    @GetMapping("/edit-profile")
    public String editProfile(HttpSession session, Model model) {
        Faculty professor = (Faculty) session.getAttribute("loggedProfessor");
        if (professor == null) {
            return "redirect:/professor/login";
        }
        model.addAttribute("faculty", professor);
        return "professor/edit-profile";
    }

    // ================= UPDATE PROFILE POST =================
    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("faculty") Faculty facultyForm,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Faculty loggedFaculty = (Faculty) session.getAttribute("loggedProfessor");
        if (loggedFaculty == null)
            return "redirect:/professor/login";

        try {
            // Service मध्ये नवीन नाव (profileImage) पाठवा
            Faculty updatedFaculty = facultyService.updateOwnProfile(
                    loggedFaculty.getFacid(),
                    facultyForm,
                    profileImage);

            session.setAttribute("loggedProfessor", updatedFaculty);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/professor/dashboard";
    }

    @PostMapping("/generate-paper")
    public String generateFinalPaper(@RequestParam("questionIds") String ids, Model model) {

        if (ids == null || ids.isEmpty()) {
            return "redirect:/professor/question-bank";
        }

        try {
            // १. String IDs ला List<Long> मध्ये रूपांतरित करा
            List<Long> idList = Arrays.stream(ids.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            // २. Repository मधून निवडलेले प्रश्न आणा
            List<QuestionBankQuestion> selectedQuestions = questionBankRepository.findAllById(idList);

            if (selectedQuestions.isEmpty()) {
                return "redirect:/professor/question-bank";
            }

            // ३. सांख्यिकी (Total Marks)
            int totalMarks = selectedQuestions.stream()
                    .mapToInt(QuestionBankQuestion::getMarks)
                    .sum();

            // ४. पेपर हेडरसाठी माहिती (पहिल्या प्रश्नावरून)
            QuestionBankQuestion first = selectedQuestions.get(0);
            model.addAttribute("course", first.getCourse());
            model.addAttribute("subject", first.getSubject());
            model.addAttribute("className", first.getClassName());
            model.addAttribute("division", first.getDivision());

            model.addAttribute("finalQuestions", selectedQuestions);
            model.addAttribute("totalQuestions", selectedQuestions.size());
            model.addAttribute("totalMarks", totalMarks);

            return "professor/final-exam-paper";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/professor/dashboard";
        }
    }
}
