package com.example.demo.session;

import com.example.demo.exceptions.InvalidSession;
import com.example.demo.exceptions.SessionExpired;
import com.example.demo.exceptions.unAuthenticated;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtSerivce jwtSerivce;

    @Autowired
    private HttpSessionService httpSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");

            if (request.getRequestURI().equals("/login")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (authorization != null && authorization.startsWith("Bearer")) {
                String token = authorization.split(" ")[1];
                if (httpSessionService.isValidSession(request.getSession().getId())) {
                    HttpSessioParam httpSessioParam = httpSessionService.getHttpSessionParam(request.getSession().getId());
                    if (jwtSerivce.validadtionToken(token, httpSessioParam.getToken())) {
                        httpSessionService.setCalculeteTimeSession(request.getSession().getId());
                    }
                }
            } else {
                httpSessionService.invalideSession(request.getSession().getId());
                request.getSession().invalidate();
                response.sendRedirect("/login");
                response.setStatus(401);
            }
        } catch (MalformedJwtException | SessionExpired | unAuthenticated | InvalidSession e) {
            httpSessionService.invalideSession(request.getSession().getId());
            request.getSession().invalidate();
            response.sendRedirect("/login");
        }
        filterChain.doFilter(request, response);
    }
}