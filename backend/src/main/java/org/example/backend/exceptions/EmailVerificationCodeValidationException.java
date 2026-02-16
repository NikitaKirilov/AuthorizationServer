package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class EmailVerificationCodeValidationException extends RuntimeException {
    public EmailVerificationCodeValidationException(String message) {
        super(message);
    }
}
