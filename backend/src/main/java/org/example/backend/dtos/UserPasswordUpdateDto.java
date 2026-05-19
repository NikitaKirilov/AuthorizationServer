package org.example.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordUpdateDto {

    private String oldPassword;

    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, message = "Длина пароля не должна быть меньше 8 символов")
    private String newPassword;
}
