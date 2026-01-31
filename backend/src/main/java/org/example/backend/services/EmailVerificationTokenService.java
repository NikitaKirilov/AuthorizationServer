package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.backend.exceptions.EmailTokenNotFoundException;
import org.example.backend.exceptions.EmailTokenVerificationException;
import org.example.backend.models.entities.EmailVerificationToken;
import org.example.backend.models.entities.User;
import org.example.backend.models.properties.EmailVerificationTokenProperties;
import org.example.backend.repositories.EmailVerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private final EmailVerificationTokenProperties tokenProperties;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public EmailVerificationToken getActiveByUser(User user) {
        return emailVerificationTokenRepository.findByUserAndActiveTrue(user)
                .orElseThrow(EmailTokenNotFoundException::new);
    }

    public String createTokenAndGetCode(User user) {
        String code = RandomStringUtils.secure().nextAlphanumeric(tokenProperties.getCodeLength());
        this.createToken(user, code);
        return code;
    }

    public void validateCode(String code, EmailVerificationToken token) {
        if (!token.isActive()) {
            throw new EmailTokenVerificationException("Email verification token is not active");
        }

        if (token.getAttemptsCount() == tokenProperties.getMaxAttempts()) {
            this.deactivateToken(token);
            throw new EmailTokenVerificationException("Too many attempts for token: " + token.getId());
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            this.deactivateToken(token);
            throw new EmailTokenVerificationException("Expired email verification token");
        }

        token.setAttemptsCount(token.getAttemptsCount() + 1);

        if (!passwordEncoder.matches(code, token.getCodeHash())) {
            throw new EmailTokenVerificationException("Invalid verification code");
        }

        this.deactivateToken(token);
    }

    private void deactivateToken(EmailVerificationToken token) {
        token.setActive(false);
        token.setUpdatedAt(Instant.now());
    }

    private void createToken(User user, String code) {
        //TODO: add task that will clear inactive tokens
        emailVerificationTokenRepository.deactivateActiveTokenForUser(user);

        Instant now = Instant.now();
        EmailVerificationToken token = new EmailVerificationToken();

        token.setId(UUID.randomUUID().toString());
        token.setUser(user);
        token.setCodeHash(passwordEncoder.encode(code));
        token.setActive(true);
        token.setCreatedAt(now);
        token.setExpiresAt(now.plus(tokenProperties.getExpirationSeconds(), SECONDS));

        emailVerificationTokenRepository.save(token);
    }
}
