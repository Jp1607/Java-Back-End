package com.example.demo.dto;

import com.example.demo.entities.Brand;
import com.fasterxml.jackson.annotation.JsonView;

public class BrandNewDTO {

    @JsonView
    private String description;

    @JsonView
    private Boolean active;

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

    public BrandNewDTO(Brand brand) {
        this.description = brand.getDescription();
        this.active = brand.getActive() == 1;
    }

    public BrandNewDTO () {}
}
