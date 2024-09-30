package com.example.demo.entities;
import com.example.demo.dto.muNewDTO;

import javax.persistence.*;

@Entity
@Table(name = "product_mu")
public class MU extends DefaultEntities {

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MU(muNewDTO mu) {
        this.description = mu.getDescription();
        this.active = mu.getActive() ? 1 : 0;
    }

    public MU() {
    }
}
