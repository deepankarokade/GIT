package com.git.Professor.Entity;

public class ProfessorLogin {

    private String username;
    private String password;

    // No-argument constructor
    public ProfessorLogin() {
        super();
    }

    // Parameterized constructor
    public ProfessorLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
