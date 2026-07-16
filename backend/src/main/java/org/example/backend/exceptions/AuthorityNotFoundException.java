package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorityNotFoundException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Authority not found by id '%s'";

    public AuthorityNotFoundException(String id) {
        super(String.format(MESSAGE_FORMAT, id));
    }
}
