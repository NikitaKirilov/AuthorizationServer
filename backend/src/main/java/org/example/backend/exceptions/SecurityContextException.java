package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
public class SecurityContextException extends RuntimeException {

    public SecurityContextException(String message) {
        super(message);
    }
}
