package com.example.demo.Enum;

public enum Flow {
    ENTRANCE("entrada"),
    EXIT("saída");

    private final String description;

    Flow(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
