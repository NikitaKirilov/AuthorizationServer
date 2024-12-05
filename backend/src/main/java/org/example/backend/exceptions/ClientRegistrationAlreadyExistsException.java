package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ClientRegistrationAlreadyExistsException extends RuntimeException {

    public ClientRegistrationAlreadyExistsException(String message) {
        super(message);
    }
}
