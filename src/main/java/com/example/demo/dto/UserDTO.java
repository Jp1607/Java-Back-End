package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class UserDTO {
    @JsonView
    private String name;
    @JsonView
    private String token;
    @JsonView
    private List<String> roles;

    public UserDTO(String name, String token, List<String> roles) {
        this.name = name;
        this.token = token;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}