package org.example.backend.dtos;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class UserDto {

    private String email;
    private boolean emailVerified;

    private String nickname;
    private String givenName;
    private String familyName;

    private LocalDate birthday;

    private boolean blocked;

    private Instant createdAt;
    private Instant updatedAt;
}
