package com.git.Professor.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Professor.Entity.Questionpaper;

public interface QuestionpaperRepositry extends JpaRepository<Questionpaper, Long> {
    List<Questionpaper> findByExamId(Long examId);

}
