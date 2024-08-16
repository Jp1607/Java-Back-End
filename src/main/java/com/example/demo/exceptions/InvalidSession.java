package com.example.demo.exceptions;

import java.io.Serializable;

public class InvalidSession extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public InvalidSession() {
    }

    public InvalidSession(String s) {
        super(s);
    }

    public InvalidSession(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidSession(Throwable throwable) {
        super(throwable);
    }
}