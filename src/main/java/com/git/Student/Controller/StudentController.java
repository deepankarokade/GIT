package com.git.Student.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.git.Student.Entity.Student;
import com.git.Student.Repository.StudentRepository;
import com.git.Student.Service.StudentService;
import com.git.Student.enumactivity.ActivityStudent;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private com.git.Admin.Service.EmailService emailService;

    // Save Student (Form Submit)
    @PostMapping("/admin/student/register")
    public ResponseEntity<?> registerStudent(

            @RequestParam("fullName") String fullName,
            @RequestParam(value = "dob", required = false) String dob,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "contactNumber", required = false) String contactNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "parentName", required = false) String parentName,
            @RequestParam(value = "parentContact", required = false) String parentContact,
            @RequestParam(value = "schoolCollegeName", required = false) String schoolCollegeName,
            @RequestParam(value = "studentClass", required = false) String studentClass,
            @RequestParam(value = "registrationId", required = false) String registrationId,
            @RequestParam(value = "preferredExamDate", required = false) String preferredExamDate,
            @RequestParam(value = "subjects", required = false) String subjects,
            @RequestParam(value = "section", required = false) String section,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "sendEmail", defaultValue = "false") boolean sendEmail

    ) {

        try {
            Student student = new Student();

            student.setFullName(fullName);
            student.setDob(dob);
            student.setGender(gender);
            student.setEmail(email);
            student.setContactNumber(contactNumber);
            student.setAddress(address);
            student.setState(state);
            student.setCity(city);
            student.setParentName(parentName);
            student.setParentContact(parentContact);
            student.setSchoolCollegeName(schoolCollegeName);
            student.setStudentClass(studentClass);
            student.setRegistrationId(registrationId);
            student.setPreferredExamDate(preferredExamDate);
            student.setSubjects(subjects);
            student.setSection(section);

            if (photo != null && !photo.isEmpty()) {
                student.setPhoto(photo.getBytes());
                student.setPhotofilename(photo.getOriginalFilename());
            }

            Student savedStudent = studentService.registerStudent(student);

            // Send payment link email to student only if requested (usually from admin
            // side)
            if (sendEmail) {
                try {
                    emailService.sendRegistrationPaymentEmail(savedStudent);
                } catch (Exception e) {
                    System.err.println("Failed to send payment email: " + e.getMessage());
                    // We don't fail the whole registration if email fails
                }
            }

            // Return JSON response with student UID for payment linking
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "Student registered successfully");
            response.put("uid", savedStudent.getUid());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", "Error occurred while registering student: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Student Login (Form Based for Studentlogin.html)
    @PostMapping("/student/login")
    public String login(
            @RequestParam String uid,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            Student student = studentService.login(uid, password);

            if (student != null) {
                // Check if account is active
                if (student.getActivityStudent() != ActivityStudent.ACTIVE) {
                    model.addAttribute("error", "Your account is not active. Please contact administrator.");
                    model.addAttribute("activeTab", "student");
                    return "student/Studentlogin";
                }

                session.setAttribute("loggedStudent", student);
                return "redirect:/student/login-options";
            } else {
                model.addAttribute("error", "Invalid UID or Password");
                model.addAttribute("activeTab", "student");
                return "student/Studentlogin";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activeTab", "student");
            return "student/Studentlogin";
        }
    }

    // Fetch All Students
    @GetMapping("/admin/student")
    @ResponseBody
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Fetch Student by UID
    @GetMapping("/admin/student/{uid}")
    @ResponseBody
    public Student getStudentByUid(@PathVariable String uid) {
        return studentService.getStudentByUid(uid);
    }

    // Edit student
    @PutMapping("/admin/student/edit/{uid}")
    public ResponseEntity<String> editStudent(
            @PathVariable String uid,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "contactNumber", required = false) String contactNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "parentName", required = false) String parentName,
            @RequestParam(value = "parentContact", required = false) String parentContact,
            @RequestParam(value = "schoolCollegeName", required = false) String schoolCollegeName,
            @RequestParam(value = "studentClass", required = false) String studentClass,
            @RequestParam(value = "registrationId", required = false) String registrationId,
            @RequestParam(value = "preferredExamDate", required = false) String preferredExamDate,
            @RequestParam(value = "subjects", required = false) String subjects,
            @RequestParam(value = "section", required = false) String section) {
        try {
            Student updatedStudent = new Student();
            updatedStudent.setFullName(fullName);
            updatedStudent.setGender(gender);
            updatedStudent.setEmail(email);
            updatedStudent.setContactNumber(contactNumber);
            updatedStudent.setAddress(address);
            updatedStudent.setState(state);
            updatedStudent.setCity(city);
            updatedStudent.setParentName(parentName);
            updatedStudent.setParentContact(parentContact);
            updatedStudent.setSchoolCollegeName(schoolCollegeName);
            updatedStudent.setStudentClass(studentClass);
            updatedStudent.setRegistrationId(registrationId);
            updatedStudent.setPreferredExamDate(preferredExamDate);
            updatedStudent.setSubjects(subjects);
            updatedStudent.setSection(section);

            studentService.editStudent(uid, updatedStudent);
            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while updating student: " + e.getMessage());
        }
    }

    // Update Student Photo
    @PutMapping("/admin/student/photo/{uid}")
    public ResponseEntity<String> updateStudentPhoto(
            @PathVariable String uid,
            @RequestParam("photo") MultipartFile photo) {
        try {
            if (photo == null || photo.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Photo file is required");
            }

            studentService.updateStudentPhoto(uid, photo.getBytes(), photo.getOriginalFilename());
            return ResponseEntity.ok("Student photo updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while updating student photo: " + e.getMessage());
        }
    }

    // ACTIVITY STATUS

    // GET Student Activity
    @GetMapping("/admin/student/{uid}/status")
    @ResponseBody
    public ActivityStudent getStatus(@PathVariable String uid) {
        return studentService.getAccountStatus(uid);
    }

    // ACTIVATE
    @PutMapping("/admin/student/{uid}/activate")
    @ResponseBody
    public void activate(@PathVariable String uid) {
        studentService.updateStudentActivityStatus(uid, ActivityStudent.ACTIVE);
    }

    // DEACTIVATE
    @PutMapping("/admin/student/{uid}/deactivate")
    @ResponseBody
    public void deactivate(@PathVariable String uid) {
        studentService.updateStudentActivityStatus(uid, ActivityStudent.INACTIVE);
    }

    // DELETE (Hard delete - removes from database)
    @DeleteMapping("/admin/student/{uid}")
    public ResponseEntity<String> deleteStudent(@PathVariable String uid) {
        try {
            studentService.deleteStudent(uid);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // COUNT ENDPOINTS

    // Get total student count
    @GetMapping("/admin/student/count/total")
    @ResponseBody
    public long getTotalStudentCount() {
        return studentService.getTotalStudentCount();
    }

    // Get active student count
    @GetMapping("/admin/student/count/active")
    @ResponseBody
    public long getActiveStudentCount() {
        return studentService.getActiveStudentCount();
    }

    // Lookup student UID by email or phone (for payment page)
    @GetMapping("/admin/student/lookup")
    public ResponseEntity<?> lookupStudentUid(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone) {

        String uid = null;

        // Try to find by email first
        if (email != null && !email.isEmpty()) {
            uid = studentService.getStudentUidByEmail(email);
        }

        // If not found, try by phone
        if (uid == null && phone != null && !phone.isEmpty()) {
            uid = studentService.getStudentUidByPhone(phone);
        }

        if (uid != null) {
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("uid", uid);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not found with provided email or phone");
        }
    }

    // Change Student password (requires current password - from dashboard)
    @PostMapping("/admin/student/{uid}/change-password")
    @ResponseBody
    public ResponseEntity<String> changePassword(
            @PathVariable String uid,
            @RequestBody Map<String, String> body) {

        try {
            studentService.changePassword(uid, body.get("currentPassword"), body.get("newPassword"));
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("Current password incorrect")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Password");
            } else if (errorMessage != null && errorMessage.contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to change password");
            }
        }
    }

    // Reset password by token (from forgot password email link - no current
    // password needed)
    @PostMapping("/admin/student/reset-password-by-token")
    public ResponseEntity<String> resetPasswordByToken(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Token and new password are required");
        }

        try {
            studentService.resetPasswordByToken(token, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Forgot password - Send reset link
    @PostMapping("/student/sendforgot-password")
    public String handleForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            String token = studentService.generateResetToken(email);
            String resetLink = "http://localhost:8081/student/change-password?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Reset Your Password");

            helper.setText("<h3>Password Reset</h3>"
                    + "<p>Click the button below to reset your password:</p>"
                    + "<a href='" + resetLink + "' "
                    + "style='display:inline-block;padding:10px 16px;background:#2563eb;color:white;"
                    + "text-decoration:none;border-radius:6px;font-weight:bold;'>"
                    + "Change Password</a>"
                    + "<p style='margin-top:10px;'>This link is valid for <b>12 hours</b> only. After that, you will need to request a new link.</p>",
                    true // enables HTML
            );

            mailSender.send(message);
            redirectAttributes.addFlashAttribute("info",
                    "Password reset link has been sent to your registered email.");
            redirectAttributes.addFlashAttribute("activeTab", "student");
            return "redirect:/student/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error sending email: " + e.getMessage());
            return "redirect:/student/forgot-password";
        }
    }

    // Login Options Page (Skip/Continue)
    @GetMapping("/student/login-options")
    public String studentLoginOptions(HttpSession session) {
        if (session.getAttribute("loggedStudent") == null) {
            return "redirect:/student/login";
        }
        return "student/student-login-options";
    }

}
