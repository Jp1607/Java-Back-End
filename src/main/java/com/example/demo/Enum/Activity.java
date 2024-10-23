package com.example.demo.Enum;

public enum Activity {
    EDIT("editou"),
    NEW("adicinou"),
    DELETE("removeu"),
    EDIT_STATE("editou estado"),
    SELL("vendeu"),
    BUY("comprou"),
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
