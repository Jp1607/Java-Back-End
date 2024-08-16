package com.example.demo.exceptions;

import java.io.Serializable;

public class SessionExpired extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public SessionExpired() {
    }

    public SessionExpired(String s) {
        super(s);
    }

    public SessionExpired(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SessionExpired(Throwable throwable) {
        super(throwable);
    }
}