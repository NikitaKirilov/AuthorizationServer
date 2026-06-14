package org.example.backend.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDeviceDto {

    private String id;

    private String details;
    private String location;

    private boolean current;

    private Instant lastLoggedAt;
}
