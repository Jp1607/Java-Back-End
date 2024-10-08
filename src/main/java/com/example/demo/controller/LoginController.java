package com.example.demo.controller;

import com.example.demo.Enum.Activity;
import com.example.demo.dto.CredentialsDTO;
import com.example.demo.dto.UserLoginDTO;
import com.example.demo.entities.Log;
import com.example.demo.entities.User;
import com.example.demo.repository.LogRepository;
import com.example.demo.session.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    @Autowired
    private LogRepository logRepository;

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public UserLoginDTO authentication(HttpServletRequest request, @RequestBody CredentialsDTO credentials) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(credentials.getUsername());
            if (encoder.matches(credentials.getPassword(), userDetails.getPassword())) {
                String token = jwtSerivce.generatorToken(userDetails);
                httpSessionService.addNewSession(request, (CustomUserDetails) userDetails, token);
                System.out.println("ativo" + userDetails.isEnabled());
                User u = new User();
                u.setId(((CustomUserDetails) userDetails).getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.LOGIN);
                log.setDate(new Date());
                System.out.println(log.getActivity().getDescription());
                logRepository.save(log);
                return new UserLoginDTO(userDetails.getUsername(), token, userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.toString());
        }
    }

    @GetMapping(value = "/logout", produces = "text/plain")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        try {

            if(token != null && token.startsWith("Bearer")){
                String t = token.split(" ")[1];
                HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
                User u = new User();
                u.setId(http.getUserDetails().getId());
                Log log = new Log();
                log.setUser(u);
                log.setActivity(Activity.LOGOUT);
                log.setDate(new Date());

                logRepository.save(log);
                httpSessionService.invalideSession(t);
            }
            return ResponseEntity.ok().body("Sessão fechada com sucesso!");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.toString());
        }
    }

    @GetMapping(value = "/sessions", produces = "application/json")
    public ResponseEntity sessionsActive(HttpServletRequest request) {
        try {
            for (Map.Entry s : httpSessionService.getActiveSessions().entrySet()) {
                System.out.println("key: " + s.getKey());
                HttpSessionParam params = (HttpSessionParam) s.getValue();
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