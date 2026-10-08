package com.identityplatform.session;

import com.identityplatform.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionAuthenticationService {

    private final SessionRepository sessionRepository;
    private final SessionTokenHasher tokenHasher;

    public SessionAuthenticationService(SessionRepository sessionRepository, SessionTokenHasher tokenHasher) {
        this.sessionRepository = sessionRepository;
        this.tokenHasher = tokenHasher;
    }

    @Transactional(readOnly = true)
    public User authenticate(String rawToken) {
        String tokenHash = tokenHasher.hash(rawToken);
        Session session = sessionRepository.findByTokenHashAndRevokedAtIsNull(tokenHash).orElseThrow(() ->new IllegalArgumentException("Invalid session"));
        if (session.isExpired()) {
            throw new IllegalArgumentException("Invalid session");
        }
        return session.getUser();
    }

}