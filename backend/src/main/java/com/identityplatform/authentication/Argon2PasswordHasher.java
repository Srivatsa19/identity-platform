package com.identityplatform.authentication;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Argon2PasswordHasher implements PasswordHasher {

    private final Argon2PasswordEncoder encoder;

    public Argon2PasswordHasher() {
        this.encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Override
    public String hash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean verify(String password, String passwordHash) {
        return encoder.matches(password, passwordHash);
    }
}