package com.example.demo.session;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class JwtSerivce {

    @Value("${security.jwt.subscriptionKey}")
    private String subscriptionKey;

    public String generatorToken(UserDetails user) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, 3); // Adiciona 3 dias
        Date expirationDate = calendar.getTime();
        return Jwts.builder().setSubject(user.getUsername()).signWith(SignatureAlgorithm.HS512, subscriptionKey).setExpiration(
                expirationDate
        ).compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(subscriptionKey).parseClaimsJws(token).getBody();
    }

    public boolean validadtionToken(String tokenRecive, String tokenClient) throws MalformedJwtException {
        if (!tokenRecive.equals(tokenClient)) {
            throw new MalformedJwtException("Token não correspondente!");
        }
        return true;
    }

    public String getLonginUser(String token) throws ExpiredJwtException {
        return getClaims(token).getSubject();
    }
}