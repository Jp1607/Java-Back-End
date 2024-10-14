package com.example.demo.session;

import com.example.demo.resources.CalculeteTime;
import com.example.demo.service.HttpSessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfiguration {

    @Bean
    public HttpSessionService httpSessionStore() {
        return new HttpSessionService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CalculeteTime calculeteTimeExpiration() {
        return new CalculeteTime();
    }
}