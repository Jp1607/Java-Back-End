package com.example.demo.dto;
import com.example.demo.entities.User;
import com.fasterxml.jackson.annotation.JsonView;

public class UserDTO {

    @JsonView
    private Long id;

    @JsonView
    private String name;

    @JsonView
    private Boolean active;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.active = user.getActive() == 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
