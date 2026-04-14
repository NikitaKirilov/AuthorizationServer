package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
@RequiredArgsConstructor
class EmailService {

    public static final String SUBJECT = "Email Verification Code";
    private static final String MESSAGE = "Your verification code: %s \n If it wasn't you - ignore this message";

    private final JavaMailSender mailSender;

    public void sendEmailVerificationCode(User user, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText(MESSAGE.formatted(code));
        message.setTo(user.getPendingEmail());
        message.setSubject(SUBJECT);

        mailSender.send(message);
    }
}
