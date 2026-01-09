package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailTokenNotFoundException extends RuntimeException {

    public EmailTokenNotFoundException(String message) {
        super(message);
    }
}

