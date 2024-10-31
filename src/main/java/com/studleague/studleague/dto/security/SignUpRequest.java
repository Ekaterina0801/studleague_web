package com.studleague.studleague.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 3, message = "Не меньше 3 знаков")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @NotBlank
    private String fullname;

    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, message = "Не меньше 8 знаков")
    private String password;

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    private String confirm;
}
