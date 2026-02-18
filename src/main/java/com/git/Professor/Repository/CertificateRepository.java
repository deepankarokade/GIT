package com.git.Professor.Repository;

import com.git.Professor.Entity.Certificate;
import com.git.Student.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByStudent(Student student);

    List<Certificate> findByStudentUid(String uid);

    long countByStudentUid(String uid);

    long countByProfessorName(String professorName);
}
