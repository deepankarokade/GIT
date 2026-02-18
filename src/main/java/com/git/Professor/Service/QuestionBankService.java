package com.git.Professor.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Professor.Entity.QuestionBankQuestion;
import com.git.Professor.Entity.DifficultySet;
import com.git.Professor.Repository.QuestionBankRepository;

@Service
public class QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    // ✅ Save question in Question Bank
    public QuestionBankQuestion saveQuestion(QuestionBankQuestion question) {
        return questionBankRepository.save(question);
    }

    // ✅ Get questions set-wise (A / B / C / D) with flexible filters
    public List<QuestionBankQuestion> getQuestionsBySet(
            String course,
            String className,
            String division,
            String subject,
            DifficultySet difficultySet) {

        // आपण Repository मध्ये बनवलेली नवीन @Query वाली मेथड इथे वापरायची आहे
        // जर पैरामीटर्स रिकामे असतील तरी ही मेथड एरर न देता सर्व डेटा आणते.
        return questionBankRepository.findFilteredQuestions(
                course != null ? course : "",
                className != null ? className : "",
                division != null ? division : "",
                subject != null ? subject : "",
                difficultySet
        );
    }

    // ✅ Get question by id
    public QuestionBankQuestion getById(Long id) {
        return questionBankRepository.findById(id).orElse(null);
    }
}