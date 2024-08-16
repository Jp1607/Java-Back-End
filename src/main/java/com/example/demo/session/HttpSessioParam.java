package com.example.demo.session;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpSession;

public class HttpSessioParam {

    private HttpSession httpSession;
    private Authentication authentication;
    private UserDetails userDetails;
    private String token;
    private Long timeExpiration = 0L;
    private Long timeCreation = 0L;

    public HttpSessioParam(HttpSession httpSession, Authentication authentication, String token, Long dataExpiration, Long dataCreated, UserDetails userDetails) {
        this.httpSession = httpSession;
        this.authentication = authentication;
        this.token = token;
        this.timeExpiration = dataExpiration;
        this.timeCreation = dataCreated;
        this.userDetails = userDetails;
    }

    public HttpSessioParam(HttpSessioParam h) {
        this.httpSession = h.httpSession;
        this.authentication = h.authentication;
        this.token = h.token;
        this.timeExpiration = h.timeExpiration;
        this.timeCreation = h.timeCreation;
        this.userDetails = h.userDetails;
    }

    public HttpSessioParam() {
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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}