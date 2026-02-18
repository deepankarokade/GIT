package com.git.Admin.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.git.Professor.Entity.Exam;

public interface AdminExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByStatus(String status);
    List<Exam> findByStatusIn(List<String> statuses);
}
