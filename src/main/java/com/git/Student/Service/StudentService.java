package com.git.Student.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.git.Student.Entity.Student;
import com.git.Student.Repository.StudentRepository;
import com.git.Student.enumactivity.ActivityStudent;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Save / Register Student (no credentials sent - will be sent after payment
    // approval)
    public Student registerStudent(Student student) {
        String uid = generateStudentUid();
        student.setUid(uid);
        student.setActivityStudent(ActivityStudent.INACTIVE);

        // Sanitize unique but optional fields: registrationId
        if (student.getRegistrationId() != null && student.getRegistrationId().trim().isEmpty()) {
            student.setRegistrationId(null);
        }

        return studentRepository.save(student);
    }

    public Student login(String uid, String password) {

        Student student = studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found with UID " + uid));

        if (!student.getPassword().equals(password)) {
            throw new RuntimeException("Invalid Password");
        }

        return student;
    }

    // Generate Student Username - uses last student's UID to prevent duplicates
    private String generateStudentUid() {
        return studentRepository.findTopByOrderByIdDesc()
                .map(lastStudent -> {
                    String lastUid = lastStudent.getUid();
                    if (lastUid != null && lastUid.startsWith("S")) {
                        try {
                            int lastNumber = Integer.parseInt(lastUid.substring(1));
                            return String.format("STU_%04d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            // Fallback if parsing fails
                            return String.format("STU_%04d", lastStudent.getId() + 1);
                        }
                    }
                    // Fallback if UID format is unexpected
                    return String.format("STU_%04d", lastStudent.getId() + 1);
                })
                .orElse("STU_0001"); // First student
    }

    // Fetch all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Fetch Student by UID
    public Student getStudentByUid(String uid) {
        return studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student editStudent(String uid, Student updatedStudent) {

        Student student = studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setFullName(updatedStudent.getFullName());
        student.setGender(updatedStudent.getGender());
        student.setEmail(updatedStudent.getEmail());
        student.setContactNumber(updatedStudent.getContactNumber());
        student.setAddress(updatedStudent.getAddress());
        student.setState(updatedStudent.getState());
        student.setCity(updatedStudent.getCity());
        student.setParentName(updatedStudent.getParentName());
        student.setParentContact(updatedStudent.getParentContact());
        student.setSchoolCollegeName(updatedStudent.getSchoolCollegeName());
        student.setStudentClass(updatedStudent.getStudentClass());

        // Handle unique but optional fields: registrationId
        String regId = updatedStudent.getRegistrationId();
        student.setRegistrationId((regId == null || regId.trim().isEmpty()) ? null : regId.trim());

        student.setPreferredExamDate(updatedStudent.getPreferredExamDate());
        student.setSubjects(updatedStudent.getSubjects());
        student.setSection(updatedStudent.getSection());

        return studentRepository.save(student);
    }

    // Update Student Photo
    public Student updateStudentPhoto(String uid, byte[] photo, String photoFilename) {
        Student student = studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setPhoto(photo);
        student.setPhotofilename(photoFilename);

        return studentRepository.save(student);
    }

    // GET Student Activity Status
    public ActivityStudent getAccountStatus(String uid) {
        return studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found"))
                .getActivityStudent();
    }

    // UPDATE Student Activity Status
    public void updateStudentActivityStatus(String uid, ActivityStudent activity) {
        Student student = studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setActivityStudent(activity);
        studentRepository.save(student);
    }

    // Get total student count
    public long getTotalStudentCount() {
        return studentRepository.count();
    }

    // Get active student count
    public long getActiveStudentCount() {
        return studentRepository.countByActivityStudent(ActivityStudent.ACTIVE);
    }

    // Delete student permanently from database
    public void deleteStudent(String uid) {
        Student student = studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found with UID: " + uid));
        studentRepository.delete(student);
    }

    // Find student UID by email
    public String getStudentUidByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(Student::getUid)
                .orElse(null);
    }

    // Find student UID by contact number
    public String getStudentUidByPhone(String phone) {
        return studentRepository.findByContactNumber(phone)
                .map(Student::getUid)
                .orElse(null);
    }

    // Find student by email or phone
    public Student findByEmailOrPhone(String email, String phone) {
        if (email != null && !email.isEmpty()) {
            return studentRepository.findByEmail(email).orElse(null);
        }
        if (phone != null && !phone.isEmpty()) {
            return studentRepository.findByContactNumber(phone).orElse(null);
        }
        return null;
    }

    // Change student password (requires current password - used for post-login
    // password change)
    public void changePassword(String uid, String currentPassword, String newPassword) {

        Student student = studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!student.getPassword().equals(currentPassword)) {
            throw new RuntimeException("Current password incorrect");
        }

        student.setPassword(newPassword);
        student.setMustResetPassword(false);
        studentRepository.save(student);
    }

    // Reset password by email (no current password required - used for forgot
    // password flow)
    public void resetPasswordByEmail(String email, String newPassword) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No student found with email: " + email));

        student.setPassword(newPassword);
        student.setMustResetPassword(false);
        studentRepository.save(student);
    }

    // Generate password reset token
    public String generateResetToken(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No student found with email: " + email));

        String token = UUID.randomUUID().toString();
        student.setResetToken(token);
        student.setResetTokenExpiry(LocalDateTime.now().plusHours(12));
        studentRepository.save(student);
        return token;
    }

    // Reset password by token (with 12-hour expiry check)
    public void resetPasswordByToken(String token, String newPassword) {
        Student student = studentRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset link."));

        if (student.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("This reset link has expired. It was valid for only 12 hours.");
        }

        student.setPassword(newPassword);
        student.setMustResetPassword(false);
        student.setResetToken(null);
        student.setResetTokenExpiry(null);
        studentRepository.save(student);
    }

    public void updateStudent(Student loggedStudent) {
        // Simply save updated student details
        studentRepository.save(loggedStudent);
    }

    // Change password from dashboard (logged-in student)
    public boolean changePasswordDashboard(String uid, String oldPassword, String newPassword) {
        // Find student by UID
        Student student = studentRepository.findByUid(uid).orElse(null);
        if (student == null)
            return false;

        // Check old password
        if (!student.getPassword().equals(oldPassword))
            return false;

        // Update to new password
        student.setPassword(newPassword);
        student.setMustResetPassword(false);
        studentRepository.save(student);

        return true;
    }

}
