package com.example.demo.entities;

import javax.persistence.*;

@Entity
@Table(name = "brand")
public class Brand extends DefaultEntities {

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
