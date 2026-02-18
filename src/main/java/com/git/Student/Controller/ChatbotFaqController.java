package com.git.Student.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.git.Student.Service.ChatbotFaqService;

@Controller
@RequestMapping("/chatbot")
public class ChatbotFaqController {

    @Autowired
    private ChatbotFaqService service;

    // Load chatbot page
    @GetMapping("/chat")
    public String chatPage() {
        return "chatbot";
    }

    // API for chat reply
    @PostMapping("/ask")
    @ResponseBody
    public String ask(@RequestParam String message) {
        return service.reply(message);
    }

    // API to load questions from DB
    @GetMapping("/questions")
    @ResponseBody
    public List<String> questions() {
        return service.getQuestions();
    }

    // ChatbotController.java madhe hi method add kara

    @PostMapping("/validate")
    @ResponseBody
    public boolean validate(@RequestParam String message) {

        if (message == null)
            return false;

        String msg = message.trim().toLowerCase();

        // ✅ SPECIAL CASE: "other" is ALWAYS valid
        if (msg.equals("other")) {
            return true;
        }

        // Length check
        if (msg.length() < 5 || msg.length() > 100) {
            return false;
        }

        // At least one vowel
        boolean hasVowels = msg.matches(".*[aeiou].*");

        // Repeating characters check
        boolean isRepeating = msg.matches("(.)\\1{4,}");

        return hasVowels && !isRepeating;
    }

}