package org.example.backend.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private int status;
    private String message;
    private LocalDateTime timestamp;
}
