package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.backend.exceptions.EmailVerificationCodeNotFoundException;
import org.example.backend.exceptions.EmailVerificationCodeValidationException;
import org.example.backend.models.entities.EmailVerificationCode;
import org.example.backend.models.entities.User;
import org.example.backend.models.properties.EmailVerificationCodeProperties;
import org.example.backend.repositories.EmailVerificationCodeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationCodeService {

    private final EmailVerificationCodeProperties codeProperties;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmailVerificationCode getActiveByUser(User user) {
        return emailVerificationCodeRepository.findByUserAndActiveTrue(user)
                .orElseThrow(EmailVerificationCodeNotFoundException::new);
    }

    public String createAndGetSourceCode(User user) {
        String code = RandomStringUtils.secure().nextAlphanumeric(codeProperties.getCodeLength());
        this.createEmailVerificationCode(user, code);
        return code;
    }

    public void validateCode(String code, EmailVerificationCode token) {
        if (!token.isActive()) {
            throw new EmailVerificationCodeValidationException("Email verification code is not active");
        }

        if (token.getAttemptsCount() == codeProperties.getMaxAttempts()) {
            throw new EmailVerificationCodeValidationException("Too many attempts. Try to request new code");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new EmailVerificationCodeValidationException("Code is expired. Try to request new code");
        }

        token.setAttemptsCount(token.getAttemptsCount() + 1);

        if (!passwordEncoder.matches(code, token.getCodeHash())) {
            throw new EmailVerificationCodeValidationException("Invalid verification code");
        }

        token.setActive(false);
    }

    private void createEmailVerificationCode(User user, String code) {
        //TODO: add task that will clear inactive tokens
        emailVerificationCodeRepository.deactivateActiveCodeForUser(user);

        Instant now = Instant.now();
        EmailVerificationCode token = new EmailVerificationCode();

        token.setId(UUID.randomUUID().toString());
        token.setUser(user);
        token.setCodeHash(passwordEncoder.encode(code));
        token.setActive(true);
        token.setExpiresAt(now.plusSeconds(codeProperties.getExpirationSeconds()));

        emailVerificationCodeRepository.save(token);
    }
}
