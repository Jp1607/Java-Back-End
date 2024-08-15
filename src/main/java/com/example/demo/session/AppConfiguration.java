package com.example.demo.session;

import com.example.demo.resources.CalculeteTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

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