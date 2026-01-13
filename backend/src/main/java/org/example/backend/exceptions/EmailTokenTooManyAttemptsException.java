package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailTokenTooManyAttemptsException extends RuntimeException {
    public EmailTokenTooManyAttemptsException(String message) {
        super(message);
    }
}
