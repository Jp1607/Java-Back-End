package com.example.demo.service;

import com.example.demo.session.CustomUserDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class JwtSerivce {

    @Value("${security.jwt.subscriptionKey}")
    private String subscriptionKey;

    public String generatorToken(CustomUserDetails user) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date expirationDate = calendar.getTime();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setId(user.getId().toString())
                .signWith(SignatureAlgorithm.HS512, subscriptionKey)
                .setExpiration(expirationDate).compact();
    }

    private Claims getClaims(String token) throws MalformedJwtException, ExpiredJwtException {
        return Jwts.parser().setSigningKey(subscriptionKey).parseClaimsJws(token).getBody();
    }

    public boolean validadtionToken(String tokenRecive, String tokenClient) throws MalformedJwtException, ExpiredJwtException {
        if (!tokenRecive.equals(tokenClient)) {
            throw new MalformedJwtException("Token não correspondente!");
        }
        if (isTokenExpired(tokenRecive)) {
            throw new ExpiredJwtException(null, null, "Token foi de arrasta");
        }
        return true;
    }

    public String getLonginUser(String token) throws ExpiredJwtException {
        return getClaims(token).getSubject();
    }

    public Long getIdUser(String token) throws ExpiredJwtException {
        return Long.parseLong(getClaims(token).getId());
    }

    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(subscriptionKey).parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}