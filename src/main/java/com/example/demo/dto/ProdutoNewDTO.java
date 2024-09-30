package com.example.demo.dto;

import com.example.demo.entities.*;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ProdutoNewDTO {

    @JsonView
    private String name;

    @JsonView
    private String description;

    @JsonView
    private String barCode;

    @JsonView
    private Boolean active;

    @JsonView
    private Brand brand;

    @JsonView
    private Group group;

    @JsonView
    private Type type;

    @JsonView
    private MU mu;

    public ProdutoNewDTO(Product p) {
        this.name = p.getName();
        this.active = p.getActive() == 1;
        this.description = p.getDescription();
        this.barCode = p.getBarCode();
        this.mu = p.getMu();
        this.brand = p.getBrand();
        this.type = p.getType();
        this.group = p.getGroup();
    }

    public ProdutoNewDTO(String name, String description, String barCode, Boolean active, Brand brand, Group group, Type type, MU mu) {
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.active = active;
        this.brand = brand;
        this.group = group;
        this.type = type;
        this.mu = mu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public MU getMu() {
        return mu;
    }

    public void setMu(MU mu) {
        this.mu = mu;
    }
}
