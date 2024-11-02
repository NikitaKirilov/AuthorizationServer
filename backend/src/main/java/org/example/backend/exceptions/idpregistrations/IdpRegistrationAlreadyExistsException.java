package org.example.backend.exceptions.idpregistrations;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class IdpRegistrationAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "Idp registration with registration id: '%s already exists'";

    public IdpRegistrationAlreadyExistsException(String registrationId) {
        super(String.format(MESSAGE, registrationId));
    }
}
