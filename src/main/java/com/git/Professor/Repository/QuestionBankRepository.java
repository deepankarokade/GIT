package com.git.Professor.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.git.Professor.Entity.DifficultySet;
import com.git.Professor.Entity.QuestionBankQuestion;

public interface QuestionBankRepository extends JpaRepository<QuestionBankQuestion, Long> {

    // एका स्पेसिफिक सेटसाठी
    @Query("SELECT q FROM QuestionBankQuestion q WHERE " +
           "(:course = '' OR q.course = :course) AND " +
           "(:className = '' OR q.className = :className) AND " +
           "(:division = '' OR q.division = :division) AND " +
           "(:subject = '' OR q.subject = :subject) AND " +
           "(q.difficultySet = :difficultySet) ORDER BY q.id Asc")
    List<QuestionBankQuestion> findFilteredQuestions(
            @Param("course") String course,
            @Param("className") String className,
            @Param("division") String division,
            @Param("subject") String subject,
            @Param("difficultySet") DifficultySet difficultySet);

    // सर्व सेट्स (A, B, C, D) एकत्र मिळवण्यासाठी
    @Query("SELECT q FROM QuestionBankQuestion q WHERE " +
           "(:course = '' OR q.course = :course) AND " +
           "(:className = '' OR q.className = :className) AND " +
           "(:division = '' OR q.division = :division) AND " +
           "(:subject = '' OR q.subject = :subject) ORDER BY q.difficultySet Asc, q.id Asc")
    List<QuestionBankQuestion> findAllSetsQuestions(
            @Param("course") String course,
            @Param("className") String className,
            @Param("division") String division,
            @Param("subject") String subject);
}