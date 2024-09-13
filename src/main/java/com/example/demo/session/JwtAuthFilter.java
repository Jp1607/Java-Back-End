package com.example.demo.session;

import com.example.demo.exceptions.InvalidSession;
import com.example.demo.exceptions.SessionExpired;
import com.example.demo.exceptions.unAuthenticated;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
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
            System.out.println("1");
            if (authorization != null && authorization.startsWith("Bearer")) {
                String token = authorization.split(" ")[1];
                if (httpSessionService.isValidSession(token)) {
                    System.out.println("2");
                    HttpSessioParam httpSessioParam = httpSessionService.getHttpSessionParam(token);
                    if (jwtSerivce.validadtionToken(token, httpSessioParam.getToken())) {
                        System.out.println("3");
                        httpSessionService.setCalculeteTimeSession(token);
                        SecurityContextHolder.getContext().setAuthentication(httpSessioParam.getAuthentication());
                    }
                }else{
                    throw new unAuthenticated("Usuário não autenticado!");
                }
            } else {
                throw new unAuthenticated("Usuário não autenticado!");
            }
        } catch (MalformedJwtException | SessionExpired | unAuthenticated | InvalidSession e) {
            response.setHeader("Content-Type", "text/plain");
            response.setStatus(401);
            request.getSession().invalidate();
            response.getOutputStream().write(e.getMessage().getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            System.out.println("6");
        }
        filterChain.doFilter(request, response);
    }
}