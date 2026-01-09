package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.EmailVerificationToken;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
@RequiredArgsConstructor
class EmailService {

    private static final String EMAIL_VERIFICATION_URL = "http://localhost:8080/registrations/confirm?token=%s";
    public static final String SUBJECT = "Email Verification Token";

    private final JavaMailSender mailSender;

    public void sendToken(EmailVerificationToken token) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText(EMAIL_VERIFICATION_URL.formatted(token.getId()));
        message.setTo(token.getUser().getEmail());
        message.setSubject(SUBJECT);

        mailSender.send(message);
    }
}
