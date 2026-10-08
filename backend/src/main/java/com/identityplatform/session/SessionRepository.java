package com.identityplatform.session;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<Session> findByTokenHashAndRevokedAtIsNull(String tokenHash);

}