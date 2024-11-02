package org.example.backend.exceptions.idpregistrations;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class IdpRegistrationNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Idp registration not found by id: '%s'";

    public IdpRegistrationNotFoundException(String registrationId) {
        super(String.format(MESSAGE, registrationId));
    }
}
