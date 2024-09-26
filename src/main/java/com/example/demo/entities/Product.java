package com.example.demo.entities;


import javax.persistence.*;

@Entity
@Table(name = "Product")
public class Product extends DefaultEntities{

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "bar_code", unique = true)
    private String barCode;

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public String getBarCode() { return barCode; }

    public void setBarCode(String barCode) { this.barCode = barCode; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
