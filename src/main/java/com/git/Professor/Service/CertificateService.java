package com.git.Professor.Service;

import com.git.Admin.Entity.Faculty;
import com.git.Admin.Repository.FacultyRepository;
import com.git.Professor.Entity.Certificate;
import com.git.Professor.Entity.Exam;
import com.git.Professor.Repository.CertificateRepository;
import com.git.Professor.Repository.ExamRepository;
import com.git.Student.Entity.Student;
import com.git.Student.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    public Certificate generateCertificate(String studentUid, Long examId, String professorUsername) {
        Student student = studentRepository.findByUid(studentUid)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        Faculty faculty = facultyRepository.findByUsername(professorUsername)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Certificate certificate = new Certificate();
        certificate.setStudent(student);
        certificate.setExam(exam);
        certificate.setCourseName(exam.getCourseName());
        certificate.setIssueDate(LocalDate.now());
        certificate.setGrade("A"); // Default grade, can be updated by professor
        certificate.setProfessorName(faculty.getFullName());
        certificate.setProfessorSignature(faculty.getSignature());

        return certificateRepository.save(certificate);
    }

    public List<Certificate> getCertificatesByStudent(String uid) {
        return certificateRepository.findByStudentUid(uid);
    }

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Certificate getCertificateById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }

    public Certificate updateCertificate(Long id, String grade, LocalDate issueDate) {
        Certificate certificate = getCertificateById(id);
        if (grade != null)
            certificate.setGrade(grade);
        if (issueDate != null)
            certificate.setIssueDate(issueDate);
        return certificateRepository.save(certificate);
    }

    public void deleteCertificate(Long id) {
        certificateRepository.deleteById(id);
    }

    public long countCertificatesByStudent(String uid) {
        return certificateRepository.countByStudentUid(uid);
    }

    public long countCertificatesByProfessor(String professorName) {
        return certificateRepository.countByProfessorName(professorName);
    }

    public long countTotalCertificates() {
        return certificateRepository.count();
    }
}
