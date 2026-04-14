package org.example.backend.dtos;

import lombok.Data;

@Data
public class UpdateUserDto {

    private String nickname;
    private String givenName;
    private String familyName;
}
