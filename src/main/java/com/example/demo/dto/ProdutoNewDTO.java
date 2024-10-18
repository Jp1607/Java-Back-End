package com.example.demo.dto;

import com.example.demo.entities.*;
import com.fasterxml.jackson.annotation.JsonView;

public class ProdutoNewDTO extends DefaultDTO{

    @JsonView
    private String name;

    @JsonView
    private String barCode;

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
        this.killed = p.getKilled() == 1;
    }

    public ProdutoNewDTO(String name, String description, String barCode, Boolean active, Brand brand, Group group, Type type, MU mu, Boolean killed) {
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.active = active;
        this.brand = brand;
        this.group = group;
        this.type = type;
        this.mu = mu;
        this.killed = killed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    @Override
    public String toString() {
        return "ProdutoNewDTO{" +
                ", active=" + active +
                ", description='" + description + '\'' +
                ", mu=" + mu +
                ", type=" + type +
                ", group=" + group +
                ", brand=" + brand +
                ", barCode='" + barCode + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
