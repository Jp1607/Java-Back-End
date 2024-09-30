package com.example.demo.entities;

import com.example.demo.dto.BrandNewDTO;

import javax.persistence.*;

@Entity
@Table(name = "product_brand")
public class Brand extends DefaultEntities {

    @Column(name = "description", unique = true)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand () {}

    public Brand(BrandNewDTO brand) {
        this.description = brand.getDescription();
        this.active = brand.getActive() ? 1 : 0;
    }
}
