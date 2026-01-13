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

    public static final String SUBJECT = "Email Verification Token";

    private final JavaMailSender mailSender;

    public void sendCode(User user, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText("Your verification code: " + code);
        message.setTo(user.getEmail());
        message.setSubject(SUBJECT);

        mailSender.send(message);
    }
}
