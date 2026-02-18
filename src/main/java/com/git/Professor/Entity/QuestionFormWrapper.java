package com.git.Professor.Entity;

import java.util.List;
import java.util.ArrayList;

public class QuestionFormWrapper {
    private List<QuestionBankQuestion> questions = new ArrayList<>();

    public List<QuestionBankQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionBankQuestion> questions) {
        this.questions = questions;
    }
}