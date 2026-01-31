package org.example.backend.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class ApiError {

    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> details = new HashMap<>();

    public void addDetail(String key, Object value) {
        details.put(key, value);
    }
}
