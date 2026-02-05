package com.git.Admin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Admin.Entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsBySubjectName(String subjectName);

    boolean existsBySubjectCode(String subjectCode);

    Optional<Subject> findBySubjectCode(String subjectCode);
}
