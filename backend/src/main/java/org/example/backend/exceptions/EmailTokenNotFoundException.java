package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailTokenNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Email token not found";

    public EmailTokenNotFoundException() {
        super(MESSAGE);
    }
}

