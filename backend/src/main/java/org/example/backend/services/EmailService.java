package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
@RequiredArgsConstructor
class EmailService {

    public static final String VERIFICATION_CODE_SUBJECT = "Email Verification Code";
    private static final String VERIFICATION_CODE_MESSAGE = "Your verification code: %s \n If it wasn't you - ignore this message";

    private static final String NEW_DEVICE_MESSAGE = "New login from %s, with device information: %s";
    public static final String NEW_DEVICE_SUBJECT = "New login";

    private final JavaMailSender mailSender;

    public void sendEmailVerificationCode(User user, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText(VERIFICATION_CODE_MESSAGE.formatted(code));
        message.setTo(user.getPendingEmail());
        message.setSubject(VERIFICATION_CODE_SUBJECT);

        mailSender.send(message);
    }

    public void sendNewDeviceNotification(User user, UserDevice userDevice) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText(NEW_DEVICE_MESSAGE.formatted(userDevice.getLocation(), userDevice.getDetails()));
        message.setTo(user.getEmail());
        message.setSubject(NEW_DEVICE_SUBJECT);

        mailSender.send(message);
    }
}
