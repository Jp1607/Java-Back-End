package com.example.demo.resources;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CalculeteTime {

    @Value("${security.jwt.expiration}")
    private String expiration;

    public CalculeteTime() {
    }

    public Long now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    public Long calcTimeToExpire() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(Long.parseLong(expiration));
        Long date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime();
        return date;
    }

    public Long calcTimeToExpire(Long timeToExpiration) {
        Long timeCalc = Date.from(LocalDateTime.now().
                plusMinutes(Long.parseLong(expiration)).
                atZone(ZoneId.systemDefault()).toInstant()).
                getTime();
        Long timeNow = Date.from(LocalDateTime.now().
                atZone(ZoneId.systemDefault()).toInstant()).getTime();
        Long timeMin = (timeCalc - timeNow);
        Long time = ((timeMin - (timeToExpiration - timeNow)) + timeToExpiration);
        return time;
    }
}