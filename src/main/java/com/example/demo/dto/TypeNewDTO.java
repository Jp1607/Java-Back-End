package com.example.demo.dto;
import com.example.demo.entities.Type;

public class TypeNewDTO extends DefaultDTO{

    public TypeNewDTO(Type type) {
        this.description = type.getDescription();
        this.active = type.getActive() == 1;
        this.killed = type.getKilled() == 1;
    }

    public TypeNewDTO() {
    }
}
