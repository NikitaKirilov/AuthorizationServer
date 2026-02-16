package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailVerificationCodeNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Email verification code not found";

    public EmailVerificationCodeNotFoundException() {
        super(MESSAGE);
    }
}

