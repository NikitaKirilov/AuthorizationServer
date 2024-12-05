package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class ClientRegistrationNotFoundException extends RuntimeException {

    public ClientRegistrationNotFoundException(String message) {
        super(message);
    }
}
