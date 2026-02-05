package com.git.Professor.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Professor.Entity.Questions;

public interface QuestionRepositry extends JpaRepository<Questions, Long> {
    List<Questions> findByQuestionPaper_Qid(Long qid);

}
