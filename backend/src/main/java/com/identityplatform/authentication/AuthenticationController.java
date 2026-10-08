package com.identityplatform.authentication;

import com.identityplatform.session.CreatedSession;
import com.identityplatform.session.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        Cookie cookie = new Cookie("SESSION", createdSession.rawToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@CookieValue(name = "SESSION", required = false) String rawToken, HttpServletResponse response) {
        if (rawToken != null) {
            sessionService.revokeSession(rawToken);
        }
        Cookie cookie = new Cookie("SESSION", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}