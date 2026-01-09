package org.example.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(code = INTERNAL_SERVER_ERROR)
public class ServerError extends RuntimeException {
    public ServerError(String message) {
        super(message);
    }

    public ServerError(Throwable cause) {
        super(cause);
    }
}
