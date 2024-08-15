package com.example.demo.controller;

import com.example.demo.dto.CredentialsDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.session.HttpSessioParam;
import com.example.demo.session.HttpSessionService;
import com.example.demo.session.JwtSerivce;
import com.example.demo.session.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private HttpSessionService httpSessionService;
    @Autowired
    private JwtSerivce jwtSerivce;

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public UserDTO authentication(HttpServletRequest request, @RequestBody CredentialsDTO credentials) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(credentials.getUsername());
            System.out.println(encoder.encode(credentials.getPassword()));
            if (encoder.matches(credentials.getPassword(), userDetails.getPassword())) {
                String token = jwtSerivce.generatorToken(userDetails);
                httpSessionService.addNewSession(request, userDetails, token);
                return new UserDTO(userDetails.getUsername(), token, userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.toString());
        }
    }

    @GetMapping(value = "/logout", produces = "text/plain")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            System.out.println("LOGOUT");
            System.out.println("key session: " + request.getSession().getId());
            httpSessionService.invalideSession(request.getSession().getId());
            return ResponseEntity.ok().body("Sessão fechada com sucesso!");
        } catch (Exception e) {
            System.out.println("Error: " + e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.toString());
        }
    }

    @GetMapping(value = "/sessions", produces = "application/json")
    public ResponseEntity sessionsActive(HttpServletRequest request) {
        try {
            for (Map.Entry s : httpSessionService.getActiveSessions().entrySet()) {
                System.out.println("key: " + s.getKey());
                HttpSessioParam params = (HttpSessioParam) s.getValue();
                System.out.println("token: " + params.getToken());
                System.out.println("session: " + params.getHttpSession());
                System.out.println("session KEY: " + params.getHttpSession().getId());
                System.out.println("auth: " + params.getAuthentication().getName());
                System.out.println("expiração : " + params.getTimeExpiration().toString());
                System.out.println("auth : " + params.getAuthentication().isAuthenticated());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping(value = "/test", produces = "text/plain")
    public ResponseEntity test(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}