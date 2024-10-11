package com.example.demo.dto;

import com.example.demo.entities.Product;
import com.fasterxml.jackson.annotation.JsonView;

public class ProdutoReturnDTO {

    @JsonView
    private Long id;

    @JsonView
    private String name;

    @JsonView
    private String description;

    @JsonView
    private String barCode;

    @JsonView
    private Boolean active;

    @JsonView
    private String brandDesc;

    @JsonView
    private String groupDesc;

    @JsonView
    private String typeDesc;

    @JsonView
    private String muDesc;

    public ProdutoReturnDTO(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        if (p.getActive() != null){
        this.active = p.getActive() == 1;
        } else {
            this.active = false;
        }
        this.description = p.getDescription();
        this.barCode = p.getBarCode();
        if (p.getBrand() != null) {
            this.brandDesc = p.getBrand().getDescription();
        }
        if (p.getGroup() != null) {
            this.groupDesc = p.getGroup().getDescription();
        }
        if (p.getType() != null) {
            this.typeDesc = p.getType().getDescription();
        }
        if (p.getMu() != null) {
            this.muDesc = p.getMu().getDescription();
        }
    }

    public ProdutoReturnDTO(Long id, String name, String description, String barCode, Boolean active, String brand, String group, String type, String mu) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.active = active;
        this.brandDesc = brand;
        this.groupDesc = group;
        this.typeDesc = type;
        this.muDesc = mu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getMuDesc() {
        return muDesc;
    }

    public void setMuDesc(String muDesc) {
        this.muDesc = muDesc;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
