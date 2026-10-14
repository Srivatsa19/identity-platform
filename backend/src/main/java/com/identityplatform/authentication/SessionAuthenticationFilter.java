package com.identityplatform.authentication;

import com.identityplatform.session.SessionAuthenticationService;
import com.identityplatform.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private static final String SESSION_COOKIE = "SESSION";

    private final SessionAuthenticationService sessionAuthenticationService;

    public SessionAuthenticationFilter(SessionAuthenticationService sessionAuthenticationService) {
        this.sessionAuthenticationService = sessionAuthenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = new SecurityContext();
        findSessionCookie(request)
                .map(Cookie::getValue)
                .ifPresent(rawToken -> authenticate(
                        rawToken,
                        securityContext
                ));
        request.setAttribute(SecurityContext.class.getName(), securityContext);
        filterChain.doFilter(request, response);
    }

    private void authenticate(String rawToken, SecurityContext securityContext
    ) {
        try {
            User user = sessionAuthenticationService.authenticate(rawToken);
            securityContext.setUser(user);
        } catch (IllegalArgumentException ignored) {
            // Invalid session means anonymous request for now.
        }
    }

    private Optional<Cookie> findSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie -> SESSION_COOKIE.equals(cookie.getName())).findFirst();
    }
}