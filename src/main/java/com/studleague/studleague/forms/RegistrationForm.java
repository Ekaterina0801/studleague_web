package com.studleague.studleague.forms;

import com.studleague.studleague.entities.security.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String fullname;
    private String email;
    public User toUser(PasswordEncoder passwordEncoder) {
        return  User.builder()
                .fullname(fullname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .username(username)
                .build();
    }
}
