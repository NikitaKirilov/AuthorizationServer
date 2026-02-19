package org.example.backend.configs;

import org.example.backend.exceptions.ActionCooldownException;
import org.example.backend.models.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class AdviceConfig {

    @ExceptionHandler(exception = ActionCooldownException.class)
    public ResponseEntity<ApiError> handleEmailVerificationCodeCooldownException(
            ActionCooldownException exception
    ) {
        ApiError apiError = new ApiError();

        apiError.setMessage(exception.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(BAD_REQUEST.value());
        apiError.addDetail("cooldown", exception.getCooldown());

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }
}
