package com.example.demo.dto;

import com.example.demo.entities.Group;
import com.fasterxml.jackson.annotation.JsonView;

public class GroupNewDTO {

    @JsonView
    private String description;

    @JsonView
    private boolean active;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public GroupNewDTO() {
    }

    public GroupNewDTO(Group group) {
        this.description = group.getDescription();
        this.active = group.getActive() == 1;
    }
}
