package org.example.backend.dtos;

import lombok.Data;

@Data
public class UpdateUserPasswordDto {

    private String oldPassword;
    private String newPassword;
}
