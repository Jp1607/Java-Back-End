package com.example.demo.session;

import com.example.demo.entities.User;
import com.example.demo.exceptions.InvalidSession;
import com.example.demo.exceptions.SessionExpired;
import com.example.demo.exceptions.unAuthenticated;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtSerivce jwtSerivce;

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private UserRepository userRepository;

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
                if (!httpSessionService.isSessionActive(token)) {
                    Long userId = jwtSerivce.getIdUser(token);
                    Optional<User> user = userRepository.findById(userId);
                    if (user.isPresent()) {
                        CustomUserDetails userDetails = new CustomUserDetails(user.get().getId(), user.get().getName(), user.get().getPassword(), user.get().getActive().compareTo(1) == 0, user.get().getAuthorities());
                        if (jwtSerivce.validadtionToken(token, user.get().getToken())) {
                            httpSessionService.addNewSession(request, userDetails, token);
                        }
                    }
                } else {
                    HttpSessionParam httpSessionParam = httpSessionService.getHttpSessionParam(token);
                    if (jwtSerivce.validadtionToken(token, httpSessionParam.getToken())) {
                        httpSessionService.setCalculeteTimeSession(token);
                        SecurityContextHolder.getContext().setAuthentication(httpSessionParam.getAuthentication());
                    }
                }
            } else {
                throw new unAuthenticated("Usuário não autenticado!");
            }
        } catch (ExpiredJwtException | MalformedJwtException | SessionExpired | unAuthenticated | InvalidSession e) {
            e.printStackTrace();
            response.setHeader("Content-Type", "text/plain");
            response.setStatus(401);
            request.getSession().invalidate();
            response.getOutputStream().write(e.getMessage().getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        filterChain.doFilter(request, response);
    }
}