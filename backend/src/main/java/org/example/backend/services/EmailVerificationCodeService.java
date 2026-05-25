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

    private final EmailService emailService;
    private final EmailVerificationCodeProperties codeProperties;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendCode(User user) {
        emailVerificationCodeRepository.deactivateActiveCodeForUser(user);
        String code = createCode(user);
        emailService.sendEmailVerificationCode(user, code);
    }

    public EmailVerificationCode getActiveByUser(User user) {
        return emailVerificationCodeRepository.findByUserAndActiveTrue(user)
                .orElseThrow(EmailVerificationCodeNotFoundException::new);
    }

    public void validateCode(String code, EmailVerificationCode token) {
        if (!token.isActive()) {
            throw new EmailVerificationCodeValidationException("Code is not active");
        }

        if (token.getAttemptsCount() == codeProperties.getMaxAttempts()) {
            throw new EmailVerificationCodeValidationException("Too many attempts. Try to request new code");
        }

        if (token.getExpiredAt().isBefore(Instant.now())) {
            throw new EmailVerificationCodeValidationException("Code is expired. Try to request new code");
        }

        token.setAttemptsCount(token.getAttemptsCount() + 1);

        if (!passwordEncoder.matches(code, token.getCodeHash())) {
            throw new EmailVerificationCodeValidationException("Invalid verification code");
        }

        token.setActive(false);
    }

    private String createCode(User user) {
        String sourceCode = RandomStringUtils.secure().nextAlphanumeric(codeProperties.getCodeLength());
        EmailVerificationCode code = new EmailVerificationCode();
        code.setId(UUID.randomUUID().toString());
        code.setUser(user);
        code.setCodeHash(passwordEncoder.encode(sourceCode));
        code.setActive(true);
        code.setExpiredAt(Instant.now().plusSeconds(codeProperties.getExpirationSeconds()));

        emailVerificationCodeRepository.save(code);

        return sourceCode;
    }
}
