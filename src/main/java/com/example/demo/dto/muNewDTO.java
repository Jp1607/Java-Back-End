package com.example.demo.dto;
import com.example.demo.entities.MU;

public class muNewDTO extends DefaultDTO{

    public muNewDTO(MU mu) {
        this.description = mu.getDescription();
        this.active = mu.getActive() == 1;
        this.killed = mu.getKilled() == 1;
    }

    public muNewDTO() {
    }
}
