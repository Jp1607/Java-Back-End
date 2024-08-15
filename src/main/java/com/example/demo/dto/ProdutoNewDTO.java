package com.example.demo.dto;

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

    public ProdutoNewDTO() { }

    public ProdutoNewDTO(String name, String description, String barCode, Boolean active) {
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.active = active;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getBarCode() { return barCode; }

    public void setBarCode(String barCode) { this.barCode = barCode; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

}
