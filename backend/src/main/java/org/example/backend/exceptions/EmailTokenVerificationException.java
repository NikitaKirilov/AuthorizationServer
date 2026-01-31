package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class EmailTokenVerificationException extends RuntimeException {
    public EmailTokenVerificationException(String message) {
        super(message);
    }
}
