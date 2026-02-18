package com.git.Student.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.git.Student.Entity.ChatbotFaq;

public interface ChatbotFaqRepository extends JpaRepository<ChatbotFaq, Long> {

    // ================= STUDENT =================
    @Query("""
            SELECT f FROM ChatbotFaq f
            WHERE f.status = true
            AND LOWER(f.question) LIKE %:msg%
            """)
    List<ChatbotFaq> searchFaq(@Param("msg") String msg);

    List<ChatbotFaq> findByStatusTrue();

}