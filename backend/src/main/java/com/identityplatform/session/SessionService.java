package com.identityplatform.session;

import com.identityplatform.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class SessionService {

    private static final Duration SESSION_DURATION = Duration.ofDays(7);

    private final SessionRepository sessionRepository;
    private final SessionTokenGenerator tokenGenerator;
    private final SessionTokenHasher tokenHasher;

    public SessionService(SessionRepository sessionRepository, SessionTokenGenerator tokenGenerator, SessionTokenHasher tokenHasher) {
        this.sessionRepository = sessionRepository;
        this.tokenGenerator = tokenGenerator;
        this.tokenHasher = tokenHasher;
    }

    public CreatedSession createSession(User user) {
        String rawToken = tokenGenerator.generate();
        String tokenHash = tokenHasher.hash(rawToken);
        Instant expiresAt = Instant.now().plus(SESSION_DURATION);
        Session session = new Session(tokenHash, user, expiresAt);
        Session savedSession = sessionRepository.save(session);
        return new CreatedSession(savedSession, rawToken);
    }

    @Transactional
    public void revokeSession(String rawToken) {
        String tokenHash = tokenHasher.hash(rawToken);
        Optional<Session> session = sessionRepository.findByTokenHashAndRevokedAtIsNull(tokenHash);
        session.ifPresent(Session::revoke);
    }

}