package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

public class UserLoginDTO {

    @JsonView
    private String name;
    @JsonView
    private String token;
    @JsonView
    private List<String> roles = new ArrayList<>();

    public UserLoginDTO(String name, String token, List<String> roles) {
        this.token = token;
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}