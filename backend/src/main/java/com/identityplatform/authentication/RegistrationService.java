package com.identityplatform.authentication;

import com.identityplatform.user.User;
import com.identityplatform.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegistrationService(
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }


    public User register(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        String passwordHash = passwordHasher.hash(password);

        User user = new User(normalizedEmail, passwordHash);

        return userRepository.save(user);
    }

}