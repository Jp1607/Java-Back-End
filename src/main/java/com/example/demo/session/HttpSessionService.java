package com.example.demo.session;

import com.example.demo.exceptions.InvalidSession;
import com.example.demo.exceptions.SessionExpired;
import com.example.demo.exceptions.unAuthenticated;
import com.example.demo.resources.CalculeteTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpSessionService {

    @Autowired
    private CalculeteTime calculeteTime;
    private Map<String, HttpSessioParam> sessions = new HashMap<>();

    public List<HttpSessioParam> getActiveSessionsParams() {
        return new ArrayList<>(sessions.values());
    }

    public Map<String, HttpSessioParam> getActiveSessions() {
        return sessions;
    }

    public void addNewSession(HttpServletRequest request, UserDetails userDetails, String token) {
        System.out.println("Login");
        isAuthenticated(userDetails.getUsername());
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        HttpSessioParam httpSessioParam = new HttpSessioParam(request.getSession(), user, token, calculeteTime.calcTimeToExpire(), calculeteTime.now(), userDetails);
        SecurityContextHolder.getContext().setAuthentication(httpSessioParam.getAuthentication());
        String a = RequestContextHolder.currentRequestAttributes().getSessionId();
        sessions.put(request.getSession().getId(), httpSessioParam);
        System.out.println("Session ID: " + request.getSession().getId());
        System.out.println("sessio active: " + sessions.size());
        System.out.println("true id: " + a);
    }

    private void isAuthenticated(String username) {
        for (HttpSessioParam http : sessions.values()) {
            if (http.getUserDetails().getUsername().equals(username)) {
                System.out.println("Usuario " + username + " foi logado em outro lugar!");
                invalideSession(http.getHttpSession().getId());
                break;
            }
        }
    }

    public void setSessession(HttpServletRequest request, UserDetails userDetails, String token) {
        HttpSessioParam httpSessioParam;
        if (isSessionActive(request.getSession().getId())) {
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            httpSessioParam = new HttpSessioParam(request.getSession(), user, token, calculeteTime.calcTimeToExpire(), sessions.get(request.getSession().getId()).getTimeCreation(), userDetails);
            SecurityContextHolder.getContext().setAuthentication(httpSessioParam.getAuthentication());
            sessions.put(request.getSession().getId(), httpSessioParam);
        }
    }

    public void setCalculeteTimeSession(String key) {
        if (isSessionActive(key)) {
            Long oldTime = sessions.get(key).getTimeExpiration();
            sessions.get(key).setTimeExpiration(calculeteTime.calcTimeToExpire(oldTime));
        }
    }

    public HttpSessioParam getActiveSession(String key) {
        return sessions.get(key);
    }

    public boolean isSessionActive(String key) {
        return sessions.containsKey(key);
    }

    public boolean isValidSession(String key) throws SessionExpired, unAuthenticated, InvalidSession {
        if (isSessionActive(key)) {
            Long timeExpiration = sessions.get(key).getTimeExpiration();
            if (calculeteTime.now().compareTo(timeExpiration) == 1) {
                sessions.get(key).getHttpSession().invalidate();
                sessions.get(key).getAuthentication().setAuthenticated(false);
                throw new SessionExpired("Sessão expirou!");
            }
            if (!sessions.get(key).getAuthentication().isAuthenticated()) {
                throw new unAuthenticated("Usuário não autenticado!");
            }
            return true;
        } else {
            throw new InvalidSession("Sessão invalida ou não iniciada!");
        }
    }

    public void invalideSession(String key) throws InvalidSession {
        if (isSessionActive(key)) {
            try {
                sessions.get(key).getHttpSession().invalidate();
            } catch (Exception e) {
                System.out.println("Sessão já está invalidada");
            }
            sessions.remove(key);
            System.out.println("Quantidade de sessões ativas: " + sessions.size());
        } else {
            throw new InvalidSession("Sessão invalida ou não iniciada!");
        }
    }

    public HttpSessioParam getHttpSessionParam(String key) throws InvalidSession {
        if (isSessionActive(key)) {
            return sessions.get(key);
        }
        throw new InvalidSession("Sessão invalida ou não iniciada!");
    }
}