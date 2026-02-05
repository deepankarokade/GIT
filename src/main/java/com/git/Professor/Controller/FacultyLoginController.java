package com.git.Professor.Controller;

import com.git.Professor.Entity.ProfessorLogin;
import com.git.Admin.Entity.Faculty;
import com.git.Admin.Service.FacultyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faculty")
public class FacultyLoginController {

    @Autowired
    private FacultyService facultyService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ProfessorLogin login) {

        try {
            Faculty faculty = facultyService.loginFaculty(
                    login.getUsername(),
                    login.getPassword());

            return ResponseEntity.ok("Login successful. Welcome to Faculty Dashboard");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
