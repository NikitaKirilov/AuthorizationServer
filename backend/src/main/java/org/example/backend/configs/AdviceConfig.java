package org.example.backend.configs;

import org.example.backend.exceptions.ActionCooldownException;
import org.example.backend.models.ApiError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError();

        apiError.setMessage("Validation failed");
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(BAD_REQUEST.value());

        Map<String, String> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (firstError, secondError) -> firstError));
        apiError.addDetail("fields", fieldErrors);

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }
}
