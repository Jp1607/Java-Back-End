package com.example.demo.service;

import com.example.demo.exceptions.InvalidSession;
import com.example.demo.resources.CalculeteTime;
import com.example.demo.session.CustomUserDetails;
import com.example.demo.session.HttpSessionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpSessionService {

    @Autowired
    private CalculeteTime calculeteTime;
    private Map<String, HttpSessionParam> sessions = new HashMap<>();

    public List<HttpSessionParam> getActiveSessionsParams() {
        return new ArrayList<>(sessions.values());
    }

    public Map<String, HttpSessionParam> getActiveSessions() {
        return sessions;
    }

    public void addNewSession(HttpServletRequest request, CustomUserDetails userDetails, String token) {
        isAuthenticated(userDetails.getUsername());

        UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());

        user.setDetails
                (new WebAuthenticationDetailsSource().buildDetails(request));

        HttpSessionParam httpSessionParam =
                new HttpSessionParam
                        (request.getSession(), user, token, calculeteTime.
                                calcTimeToExpire(), calculeteTime.now(), userDetails);

        SecurityContextHolder.getContext().
                setAuthentication(httpSessionParam.getAuthentication());
        sessions.put(token, httpSessionParam);
    }

    private void isAuthenticated(String username) {
        for (HttpSessionParam http : sessions.values()) {
            if (http.getUserDetails().getUsername().equals(username)) {
                invalideSession(http.getToken());
                break;
            }
        }
    }

    public void setCalculeteTimeSession(String token) {
        if (isSessionActive(token)) {
            Long oldTime = sessions.get(token).getTimeExpiration();
            sessions.get(token).setTimeExpiration(calculeteTime.calcTimeToExpire(oldTime));
        }
    }

    public boolean isSessionActive(String id) {
        return sessions.containsKey(id);
    }

    public void invalideSession(String token) throws InvalidSession {
        if (isSessionActive(token)) {
            try {
                sessions.get(token).getHttpSession().invalidate();
            } catch (Exception ignored) {

            }
            sessions.remove(token);
        } else {
            throw new InvalidSession("Sessão invalida ou não iniciada!");
        }
    }

    public HttpSessionParam getHttpSessionParam(String key) throws InvalidSession {
        if (isSessionActive(key)) {
            return sessions.get(key);
        }
        throw new InvalidSession("Sessão invalida ou não iniciada!");
    }
}