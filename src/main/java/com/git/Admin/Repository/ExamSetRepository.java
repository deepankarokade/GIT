package com.git.Admin.Repository;

import com.git.Admin.Entity.ExamSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamSetRepository extends JpaRepository<ExamSet, Long> {
    Optional<ExamSet> findBySetName(String setName);

    Optional<ExamSet> findBySetId(String setId);

    Optional<ExamSet> findTopByOrderByIdDesc();
}
