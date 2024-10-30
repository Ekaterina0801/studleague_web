package com.studleague.studleague.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    @Size(min = 3, message = "Не меньше 3 знаков")
    private String username;

    @Size(min = 8, message = "Не меньше 8 знаков")
    private String password;

    @NotBlank
    private String fullname;

    @Email
    private String email;
    private List<Long> rolesIds;


}
