package com.example.demo.Enum;

public enum Activity {
    EDIT("editou"),
    NEW("adicinou"),
    DELETE("removeu"),
    LOGIN("logou"),
    LOGOUT("deslogou");

    private final String description;

    Activity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
