package com.puthi.commentapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserDetailsService uds;

    public JwtAuthFilter(JwtService jwt, UserDetailsService uds) {
        this.jwt = jwt; this.uds = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            try {
                var username = jwt.validateAndGetSubject(h.substring(7));
                var user = uds.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ignored) { /* leave unauthenticated */ }
        }
        chain.doFilter(req, res);
    }
}
