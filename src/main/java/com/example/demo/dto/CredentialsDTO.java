package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class CredentialsDTO {
    @JsonView
    private String username;
    @JsonView
    private String password;

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