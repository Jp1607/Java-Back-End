package com.example.demo.session;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpSession;

public class HttpSessionParam {

    private HttpSession httpSession;
    private Authentication authentication;
    private CustomUserDetails userDetails;
    private String token;
    private Long timeExpiration = 0L;
    private Long timeCreation = 0L;

    public HttpSessionParam(HttpSession httpSession, Authentication authentication, String token, Long dataExpiration, Long dataCreated, CustomUserDetails userDetails) {
        this.httpSession = httpSession;
        this.authentication = authentication;
        this.token = token;
        this.timeExpiration = dataExpiration;
        this.timeCreation = dataCreated;
        this.userDetails = userDetails;
    }

    public HttpSessionParam(HttpSessionParam h) {
        this.httpSession = h.httpSession;
        this.authentication = h.authentication;
        this.token = h.token;
        this.timeExpiration = h.timeExpiration;
        this.timeCreation = h.timeCreation;
        this.userDetails = h.userDetails;
    }

    public HttpSessionParam() {
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTimeExpiration() {
        return timeExpiration;
    }

    public void setTimeExpiration(Long timeExpiration) {
        this.timeExpiration = timeExpiration;
    }

    public Long getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(Long timeCreation) {
        this.timeCreation = timeCreation;
    }

    public CustomUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(CustomUserDetails userDetails) {
        this.userDetails = userDetails;
    }
}