package com.example.demo.exceptions;

import java.io.Serializable;

public class unAuthenticated extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public unAuthenticated() {
    }

    public unAuthenticated(String s) {
        super(s);
    }

    public unAuthenticated(String s, Throwable throwable) {
        super(s, throwable);
    }

    public unAuthenticated(Throwable throwable) {
        super(throwable);
    }
}