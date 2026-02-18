package com.git.Student.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Student.Entity.ChatbotFaq;
import com.git.Student.Repository.ChatbotFaqRepository;

@Service
public class ChatbotFaqService {

    @Autowired
    private ChatbotFaqRepository repository;

    /* ===================== STUDENT CHATBOT ===================== */
    public String reply(String message) {

        if (message == null) {
            return "INVALID";
        }

        String msg = message.trim().toLowerCase();

        // Special case
        if (msg.equals("other")) {
            return "Your question has been sent to admin. Please wait for response.";
        }

        // Greeting
        if (msg.matches("hi|hello|good morning|good evening")) {
            return "Hello 👋 How can I help you with exams?";
        }

        // Invalid random input
        if (msg.length() < 5 || !msg.contains(" ")) {
            return "INVALID";
        }

        List<ChatbotFaq> faqs = repository.searchFaq(msg);

        if (!faqs.isEmpty()) {
            return faqs.get(0).getAnswer();
        }

        return "Your question has been sent to admin. Please wait for response.";
    }

    public List<String> getQuestions() {
        return repository.findByStatusTrue()
                .stream()
                .map(ChatbotFaq::getQuestion)
                .distinct()
                .toList();
    }

}