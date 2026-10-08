package com.identityplatform.session;

public record CreatedSession(
        Session session,
        String rawToken
) {
}