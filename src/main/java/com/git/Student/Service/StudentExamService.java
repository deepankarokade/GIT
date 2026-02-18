package com.git.Student.Service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Professor.Entity.Questionpaper;
import com.git.Professor.Repository.QuestionpaperRepositry;

@Service
public class StudentExamService {

    @Autowired
    private QuestionpaperRepositry repo;

    public Questionpaper getQuestionPaperById(Long paperId) {
        return repo.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Question Paper not found"));
    }
    
    public List<String[]> parseMatchPairs(String matchPairsStr) {
        // Example input: "Apple-Red|Banana-Yellow|Grapes-Purple"
        List<String[]> list = new ArrayList<>();
        String[] pairs = matchPairsStr.split("\\|");
        for (String pair : pairs) {
            String[] splitPair = pair.split("-");
            if (splitPair.length == 2) {
                list.add(splitPair);
            }
        }
        return list;
}
}

