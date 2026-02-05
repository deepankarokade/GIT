package com.git.Professor.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello from TestController!";
    }
    
    @GetMapping("/questionpaper-test")
    @ResponseBody
    public String questionpaperTest() {
        return "Questionpaper test endpoint working!";
    }
}