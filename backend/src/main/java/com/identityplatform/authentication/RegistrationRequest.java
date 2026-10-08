package com.identityplatform.authentication;

public record RegistrationRequest(
        String email,
        String password
) {
}