package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class EmailIsAlreadyVerifiedException extends RuntimeException {

    private static final String MESSAGE = "Email is already verified";

    public EmailIsAlreadyVerifiedException() {
        super(MESSAGE);
    }
}
