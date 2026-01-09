package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.EmailIsAlreadyVerifiedException;
import org.example.backend.exceptions.EmailTokenExpiredException;
import org.example.backend.exceptions.EmailTokenNotFoundException;
import org.example.backend.models.entities.EmailVerificationToken;
import org.example.backend.models.entities.User;
import org.example.backend.models.properties.EmailVerificationTokenProperties;
import org.example.backend.repositories.EmailVerificationTokenRepository;
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

    public EmailVerificationToken getById(String id) {
        return emailVerificationTokenRepository.findById(id)
                .orElseThrow(() -> new EmailTokenNotFoundException("Email confirmation token not found"));
    }

    public EmailVerificationToken createToken(User user) {
        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyVerifiedException("Email is already verified for user: " + user.getId());
        }

        Instant now = Instant.now();
        List<EmailVerificationToken> tokens = user.getEmailVerificationTokens();
        if (tokens.size() >= MAX_TOKENS_PER_USER) {
            tokens.stream()
                    .min(Comparator.comparing(EmailVerificationToken::getCreatedAt))
                    .ifPresent(emailVerificationTokenRepository::delete);
        }

        tokens.stream()
                .filter(EmailVerificationToken::isActive)
                .forEach(token -> {
                    token.setActive(false);
                    token.setUpdatedAt(now);
                    emailVerificationTokenRepository.save(token);
                });

        EmailVerificationToken token = new EmailVerificationToken();
        token.setId(UUID.randomUUID().toString());
        token.setUser(user);
        token.setActive(true);
        token.setCreatedAt(now);
        token.setExpiresAt(now.plus(tokenProperties.getExpirationSeconds(), SECONDS));

        return emailVerificationTokenRepository.save(token);
    }

    public void validateToken(EmailVerificationToken token) {
        if (!token.isActive() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new EmailTokenExpiredException("Expired email confirmation token");
        }

        token.setUpdatedAt(Instant.now());
        token.setActive(false);

        emailVerificationTokenRepository.save(token);
    }
}
