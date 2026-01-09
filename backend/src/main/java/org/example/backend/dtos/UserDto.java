package org.example.backend.dtos;

import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String password;

    private String name;
    private String givenName;
    private String familyName;
}
