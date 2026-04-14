package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class EmailIsAlreadyTakenException extends RuntimeException {

    private static final String MESSAGE = "There is already an Account associated with this email.";

    public EmailIsAlreadyTakenException() {
        super(MESSAGE);
    }
}
