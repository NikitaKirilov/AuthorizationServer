package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoleAlreadyExists extends RuntimeException {
    public RoleAlreadyExists(String message) {
        super(message);
    }
}
