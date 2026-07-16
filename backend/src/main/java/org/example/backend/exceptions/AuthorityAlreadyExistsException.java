package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthorityAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Authority with resource '%s', name '%s' and id not '%s' already exists";

    public AuthorityAlreadyExistsException(String resource, String name, String id) {
        super(String.format(MESSAGE_FORMAT, resource, name, id));
    }
}
