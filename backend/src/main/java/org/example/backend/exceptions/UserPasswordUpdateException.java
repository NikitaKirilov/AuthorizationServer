package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class UserPasswordUpdateException extends RuntimeException {
    public UserPasswordUpdateException(String message) {
        super(message);
    }
}
