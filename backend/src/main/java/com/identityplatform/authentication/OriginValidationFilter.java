package com.identityplatform.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class OriginValidationFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of("http://localhost:5173");

    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        if (SAFE_METHODS.contains(method)) {
            filterChain.doFilter(request, response);
            return;
        }
        String origin = request.getHeader("Origin");
        if (origin == null || !ALLOWED_ORIGINS.contains(origin)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid request origin");
            return;
        }
        filterChain.doFilter(request, response);
    }
}