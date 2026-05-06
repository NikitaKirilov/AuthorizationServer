package org.example.backend.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {

    private String email;

    private String nickname;
    private String givenName;
    private String familyName;

    private Instant createdAt;
    private Instant updatedAt;
}
