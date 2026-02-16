package org.example.backend.repositories;

import org.example.backend.models.entities.EmailVerificationCode;
import org.example.backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, String> {

    @Modifying
    @Query("UPDATE EmailVerificationCode c " +
            "SET c.active = false, c.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE c.user = :user AND c.active = true")
    void deactivateActiveCodeForUser(User user);

    Optional<EmailVerificationCode> findByUserAndActiveTrue(User user);
}
