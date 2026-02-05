package com.git.Professor.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.git.Professor.Entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

}
