package org.example.backend.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class RoleDto {

    private String id;

    private String resource;
    private String name;
    private String description;

    private Instant createdAt;
    private Instant updatedAt;
}
