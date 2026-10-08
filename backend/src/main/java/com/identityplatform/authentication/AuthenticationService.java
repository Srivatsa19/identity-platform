package com.identityplatform.authentication;

import com.identityplatform.session.CreatedSession;
import com.identityplatform.session.Session;
import com.identityplatform.session.SessionService;
import com.identityplatform.user.User;
import com.identityplatform.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final SessionService sessionService;

    public AuthenticationService(UserRepository userRepository, PasswordHasher passwordHasher, SessionService sessionService) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.sessionService = sessionService;
    }

    public CreatedSession authenticate(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();
        User user = userRepository.findByEmail(normalizedEmail).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordHasher.verify(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return sessionService.createSession(user);
    }
}