package com.studleague.studleague.services.implementations.security;

import com.studleague.studleague.dto.security.JwtAuthenticationResponse;
import com.studleague.studleague.dto.security.SignInRequest;
import com.studleague.studleague.dto.security.SignUpRequest;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.repository.security.RoleRepository;
import io.netty.handler.codec.http.cookie.Cookie;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final RabbitTemplate rabbitTemplate;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullname(request.getFullname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findByNameIgnoreCase("ROLE_EDITOR").orElseThrow())
                .build();
        userService.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = userService.findUserByUsername(request.getUsername());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    public JwtAuthenticationResponse refreshAccessToken(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken);
        User user = userService.findUserByUsername(username);

        if (jwtService.isTokenValid(refreshToken, user)) {
            String newAccessToken = jwtService.generateAccessToken(user);
            return new JwtAuthenticationResponse(newAccessToken, refreshToken);
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    // Запрос на сброс пароля
    public void requestPasswordReset(String email) throws NotFoundException {
        User user = userService.getByEmail(email);
        String token = jwtService.generateResetPasswordToken(user);

        Map<String, Object> message = new HashMap<>();
        message.put("email", email);
        message.put("token", token);

        rabbitTemplate.convertAndSend("exchange.direct", "resetPasswordRoutingKey", message);
    }


    // Сброс пароля
    public void resetPassword(String token, String newPassword) {
        String username = jwtService.extractUserName(token);
        User user = userService.findUserByUsername(username);

        if (jwtService.isTokenValid(token, user)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.save(user);
        } else {
            throw new RuntimeException("Invalid or expired reset token");
        }
    }
}
