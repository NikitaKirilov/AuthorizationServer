package org.example.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {

    @Pattern(regexp = "^\\p{L}+(\\s\\p{L}+)?$", message = "Псевдоним должен содержать только буквы")
    @NotBlank(message = "Псевдоним не может быть пустым")
    private String nickname;

    @Pattern(regexp = "^\\p{L}+", message = "Имя должно содержать только буквы")
    @NotBlank(message = "Имя не может быть пустым")
    private String givenName;

    @Pattern(regexp = "^\\p{L}+", message = "Фамилия должна содержать только буквы")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String familyName;

    @NotNull(message = "Некорректная дата")
    @Past(message = "Некорректная дата")
    private LocalDate birthday;
}
