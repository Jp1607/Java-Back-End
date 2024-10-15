package com.example.demo.entities;


import com.example.demo.dto.ProdutoNewDTO;
import com.example.demo.dto.ProdutoReturnDTO;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product extends DefaultEntities {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "bar_code", unique = true)
    private int barCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private Type type;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mu_id", referencedColumnName = "id")
    private MU mu;

    public Product () {}

    public Product (ProdutoNewDTO prodDTO) {

        this.name = prodDTO.getName();
        this.description = prodDTO.getDescription();
        this.barCode = prodDTO.getBarCode();
        this.active = prodDTO.getActive() ? 1 : 0;
        this.mu = prodDTO.getMu();
        this.brand = prodDTO.getBrand();
        this.group = prodDTO.getGroup();
        this.type = prodDTO.getType();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBarCode() {
        return barCode;
    }

    public void setBarCode(int barCode) {
        this.barCode = barCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
