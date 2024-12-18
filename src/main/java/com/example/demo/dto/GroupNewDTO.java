package com.example.demo.dto;

import com.example.demo.entities.Group;

public class GroupNewDTO extends  DefaultDTO{

    public GroupNewDTO() {
    }

    public GroupNewDTO(Group group) {
        this.description = group.getDescription();
        this.active = group.getActive() == 1;
        this.killed = group.getKilled() == 1;
    }
}
