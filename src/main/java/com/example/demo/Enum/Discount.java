package com.example.demo.Enum;

public enum Discount {
    PERCENTAGE("porcentagem"),
    DECIMAL("decimal");

    private final String description;

    Discount(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
