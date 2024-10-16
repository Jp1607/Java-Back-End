package com.example.demo.dto;
import com.example.demo.entities.Brand;

public class BrandNewDTO extends DefaultDTO {

    public BrandNewDTO(Brand brand) {
        this.description = brand.getDescription();
        this.active = brand.getActive() == 1;
        this.killed = brand.getKilled() == 1;

    }

    public BrandNewDTO() {
    }
}
