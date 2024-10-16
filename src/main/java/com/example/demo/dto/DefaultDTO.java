package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class DefaultDTO {

    @JsonView
    protected String description;

    @JsonView
    protected Boolean active;

    @JsonView
    protected Boolean killed;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getKilled() {
        return killed;
    }

    public void setKilled(Boolean killed) {
        this.killed = killed;
    }
}
