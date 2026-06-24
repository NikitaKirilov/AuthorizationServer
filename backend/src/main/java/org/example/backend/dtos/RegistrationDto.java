package org.example.backend.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationDto {

    private String email;
    private String password;

    private String nickname;
    private String givenName;
    private String familyName;

    private LocalDate birthday;
}
