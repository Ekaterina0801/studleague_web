package com.studleague.studleague.forms;

import com.studleague.studleague.dto.UserDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;


@Data
public class RegistrationForm {

    @Size(min = 3, message = "Не меньше 3 знаков")
    private String username;

    @Size(min = 8, message = "Не меньше 8 знаков")
    private String password;

    @NotBlank
    private String confirm;

    @NotBlank(message = "Полное имя не должно быть пустым")
    private String fullname;

    @Email(message = "Неверный формат электронной почты")
    private String email;

    public UserDTO toUserDTO(PasswordEncoder passwordEncoder) {
        return UserDTO.builder()
                .fullname(fullname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .username(username)
                .build();
    }
}
