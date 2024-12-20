package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.security.JwtAuthenticationResponse;
import com.studleague.studleague.dto.security.RefreshTokenRequest;
import com.studleague.studleague.dto.security.SignInRequest;
import com.studleague.studleague.dto.security.SignUpRequest;
import com.studleague.studleague.services.implementations.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;
    //@Value("${frontend.url}")
    private String frontendUrl = "http://localhost:5174/sign-in";

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshAccessToken(request.getRefreshToken()));
    }

    @Operation(summary = "Запрос на сброс пароля")
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) throws NotFoundException {
        authenticationService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @Operation(summary = "Сброс пароля")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        authenticationService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password successfully reset");
    }

    @PostMapping(value = "/confirm-reset-password")
    @Operation(summary = "Подтверждение сброса пароля")
    public ResponseEntity<Map<String, String>> confirmResetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {

        authenticationService.resetPassword(token, newPassword);

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", frontendUrl);

        return ResponseEntity.ok(response);
    }


}

