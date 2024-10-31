package com.studleague.studleague.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class SignInRequest {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 3, message = "Не меньше 3 знаков")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;


    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, message = "Не меньше 8 знаков")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
