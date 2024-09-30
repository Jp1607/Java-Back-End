package com.example.demo.dto;

import com.example.demo.entities.MU;
import com.fasterxml.jackson.annotation.JsonView;

public class muNewDTO {

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

    public muNewDTO(MU mu) {
        this.description = mu.getDescription();
        this.active = mu.getActive() == 1;
    }

    public muNewDTO() {
    }
}
