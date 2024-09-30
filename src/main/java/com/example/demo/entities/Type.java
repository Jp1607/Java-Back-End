package com.example.demo.entities;
import com.example.demo.dto.TypeNewDTO;

import javax.persistence.*;

@Entity
@Table(name = "product_type")
public class Type extends DefaultEntities {

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type(TypeNewDTO type) {
        this.description = type.getDescription();
        this.active = type.getActive() ? 1 : 0;
    }

    public Type() {
    }
}
