package com.puthi.commentapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RequestLogFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {
        long t0 = System.currentTimeMillis();
        try {
            chain.doFilter(req, res);
       } finally {
        long ms = System.currentTimeMillis() - t0;
        logger.info("{} {} -> {} ({} ms)", req.getMethod(), req.getRequestURI(), res.getStatus(), ms);
    }
    }
}
