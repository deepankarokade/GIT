package com.git.Professor.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.git.Professor.Entity.ExamPreparationForm;

public interface ExamPreparationRepository extends JpaRepository<ExamPreparationForm, Long> {
	
	
	List<ExamPreparationForm> findBySubjectNameContainingIgnoreCase(String subjectName);
	// In ExamPreparationRepository.java
	List<ExamPreparationForm> findTop3ByOrderByIdDesc();
	
	@Query("SELECT DISTINCT e.subjectName FROM ExamPreparationForm e")
	List<String> findDistinctSubjectNames();

	
}
