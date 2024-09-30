package com.example.demo.entities;

import javax.persistence.*;

@MappedSuperclass
public class DefaultEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id = 0L;

    @Column(name = "active", columnDefinition="int(1)" )
    protected Integer active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
