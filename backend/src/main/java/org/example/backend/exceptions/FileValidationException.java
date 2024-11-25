package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(code = BAD_REQUEST)
public class FileValidationException extends RuntimeException {

    public FileValidationException(String message) {
        super(message);
    }
}
