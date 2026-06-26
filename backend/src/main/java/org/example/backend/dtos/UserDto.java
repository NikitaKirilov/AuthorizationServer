package org.example.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class UserDto {

    private String id;

    private String email;
    private boolean emailVerified;

    @NotBlank(message = "Nickname required")
    private String nickname;
    @NotBlank(message = "Given name required")
    private String givenName;
    @NotBlank(message = "Family name required")
    private String familyName;

    @NotNull(message = "Birthday required")
    private LocalDate birthday;

    private boolean blocked;

    private Instant createdAt;
    private Instant updatedAt;
}
