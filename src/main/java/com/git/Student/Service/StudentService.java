package com.git.Student.Service;

import java.util.List;

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

        // Convert empty strings to null for unique-constrained fields
        String email = student.getEmail();
        if (email != null && email.trim().isEmpty()) {
            student.setEmail(null);
        }
        String regId = student.getRegistrationId();
        if (regId != null && regId.trim().isEmpty()) {
            student.setRegistrationId(null);
        }

        return studentRepository.save(student);
    }

    public Student login(String uid, String password) {

        Student student = studentRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Admin not found with UID " + uid));

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
                            return String.format("S%03d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            // Fallback if parsing fails
                            return String.format("S%03d", lastStudent.getId() + 1);
                        }
                    }
                    // Fallback if UID format is unexpected
                    return String.format("S%03d", lastStudent.getId() + 1);
                })
                .orElse("S001"); // First student
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
        // Convert empty email to null to avoid unique constraint violations
        String email = updatedStudent.getEmail();
        student.setEmail((email != null && email.trim().isEmpty()) ? null : email);
        student.setContactNumber(updatedStudent.getContactNumber());
        student.setAddress(updatedStudent.getAddress());
        student.setState(updatedStudent.getState());
        student.setCity(updatedStudent.getCity());
        student.setParentContact(updatedStudent.getParentContact());
        student.setSchoolCollegeName(updatedStudent.getSchoolCollegeName());
        student.setStudentClass(updatedStudent.getStudentClass());
        // Convert empty registrationId to null to avoid unique constraint violations
        String regId = updatedStudent.getRegistrationId();
        student.setRegistrationId((regId != null && regId.trim().isEmpty()) ? null : regId);
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
        studentRepository.save(student);
    }

    // Reset password by email (no current password required - used for forgot
    // password flow)
    public void resetPasswordByEmail(String email, String newPassword) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No student found with email: " + email));

        student.setPassword(newPassword);
        studentRepository.save(student);
    }

}
