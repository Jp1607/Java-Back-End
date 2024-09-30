package com.example.demo.dto;

import com.example.demo.entities.Type;
import com.fasterxml.jackson.annotation.JsonView;

public class TypeNewDTO {

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

    public TypeNewDTO(Type type) {
        this.description = type.getDescription();
        this.active = type.getActive() == 1;
    }

    public TypeNewDTO() {
    }
}
