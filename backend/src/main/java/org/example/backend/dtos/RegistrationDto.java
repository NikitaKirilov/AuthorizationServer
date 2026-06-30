package org.example.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationDto {

    @Email(message = "Почта должна быть в правильном формате")
    @NotBlank(message = "Почта не может быть пустой")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Пароль слишком слабый")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @Pattern(regexp = "^\\p{L}+(\\s\\p{L}+)?$", message = "Псевдоним должен содержать только буквы")
    @NotBlank(message = "Псевдоним не может быть пустым")
    private String nickname;
    @Pattern(regexp = "^\\p{L}+", message = "Имя должно содержать только буквы")
    @NotBlank(message = "Имя не может быть пустым")
    private String givenName;
    @Pattern(regexp = "^\\p{L}+", message = "Фамилия должна содержать только буквы")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String familyName;

    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Некорректная дата")
    private LocalDate birthday;
}
