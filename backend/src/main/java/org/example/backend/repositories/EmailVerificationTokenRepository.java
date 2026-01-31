package org.example.backend.repositories;

import org.example.backend.models.entities.EmailVerificationToken;
import org.example.backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, String> {

    @Modifying
    @Query("UPDATE EmailVerificationToken t " +
            "SET t.active = false, t.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE t.user = :user AND t.active = true")
    void deactivateActiveTokenForUser(User user);

    Optional<EmailVerificationToken> findByUserAndActiveTrue(User user);
}
