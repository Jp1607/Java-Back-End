package com.example.demo.entities;
import com.example.demo.dto.GroupNewDTO;
import javax.persistence.*;

@Entity
@Table(name = "product_group")
public class Group extends DefaultEntities {

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Group(GroupNewDTO group) {
        this.description = group.getDescription();
        this.active = group.getActive() ? 1 : 0;
    }

    public Group() {
    }
}