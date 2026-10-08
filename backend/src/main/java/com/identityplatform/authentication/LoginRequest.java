package com.identityplatform.authentication;

public record LoginRequest(
        String email,
        String password
) {
}