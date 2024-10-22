package com.example.demo.Enum;

public enum Payment {
    CREDIT_CARD("crédito"),
    DEBIT_CARD("débito");

    private final String description;

    Payment(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
