package com.identityplatform.authentication;

import com.identityplatform.session.CreatedSession;
import com.identityplatform.session.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final SessionService sessionService;

    public AuthenticationController(AuthenticationService authenticationService, SessionService sessionService) {
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        CreatedSession createdSession = authenticationService.authenticate(request.email(), request.password());
        ResponseCookie cookie = ResponseCookie
                .from("SESSION", createdSession.rawToken())
                .httpOnly(true)
                .secure(false) // Local HTTP development only
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@CookieValue(name = "SESSION", required = false) String rawToken, HttpServletResponse response) {
        if (rawToken != null) {
            sessionService.revokeSession(rawToken);
        }

        ResponseCookie cookie = ResponseCookie
                .from("SESSION", "")
                .httpOnly(true)
                .secure(false) // Local HTTP development only
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

    }

}