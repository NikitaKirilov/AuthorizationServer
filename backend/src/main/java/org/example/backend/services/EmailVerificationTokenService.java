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
            throw new EmailTokenVerificationException("Too many attempts. Try to request new code");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new EmailTokenVerificationException("Code is expired. Try to request new code");
        }

        token.setAttemptsCount(token.getAttemptsCount() + 1);

        if (!passwordEncoder.matches(code, token.getCodeHash())) {
            throw new EmailTokenVerificationException("Invalid verification code");
        }

        token.setActive(false);
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
        token.setExpiresAt(now.plusSeconds(tokenProperties.getExpirationSeconds()));

        emailVerificationTokenRepository.save(token);
    }
}
