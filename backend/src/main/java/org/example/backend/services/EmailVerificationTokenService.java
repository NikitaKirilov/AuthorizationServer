package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.backend.exceptions.EmailIsAlreadyVerifiedException;
import org.example.backend.exceptions.EmailTokenExpiredException;
import org.example.backend.exceptions.EmailTokenIsNotActiveException;
import org.example.backend.exceptions.EmailTokenTooManyAttemptsException;
import org.example.backend.exceptions.InvalidEmailVerificationCode;
import org.example.backend.models.entities.EmailVerificationToken;
import org.example.backend.models.entities.User;
import org.example.backend.models.properties.EmailVerificationTokenProperties;
import org.example.backend.repositories.EmailVerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private static final int MAX_TOKENS_PER_USER = 5;

    private final EmailVerificationTokenProperties tokenProperties;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public String createTokenAndGetCode(User user) {
        String code = RandomStringUtils.secure().nextAlphanumeric(tokenProperties.getCodeLength());
        this.createToken(user, code);
        return code;
    }

    public void validateCode(String code, EmailVerificationToken token) {
        if (!token.isActive()) {
            throw new EmailTokenIsNotActiveException("Email token is not active");
        }

        if (token.getAttemptsCount() == tokenProperties.getMaxAttempts()) {
            this.deactivateToken(token);
            throw new EmailTokenTooManyAttemptsException("Too many attempts for token: " + token.getId());
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            this.deactivateToken(token);
            throw new EmailTokenExpiredException("Expired email confirmation token");
        }

        token.setAttemptsCount(token.getAttemptsCount() + 1);

        if (!passwordEncoder.matches(code, token.getCodeHash())) {
            throw new InvalidEmailVerificationCode("Invalid email verification code");
        }

        this.deactivateToken(token);
    }

    private void deactivateToken(EmailVerificationToken token) {
        token.setActive(false);
        token.setUpdatedAt(Instant.now());
    }

    private void createToken(User user, String code) {
        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyVerifiedException("Email is already verified for user: " + user.getId());
        }

        Instant now = Instant.now();
        List<EmailVerificationToken> tokens = user.getEmailVerificationTokens();
        if (tokens.size() >= MAX_TOKENS_PER_USER) {
            tokens.stream().min(Comparator.comparing(EmailVerificationToken::getCreatedAt)).ifPresent(emailVerificationTokenRepository::delete);
        }

        user.getActiveEmailVerificationToken().ifPresent(this::deactivateToken);

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
