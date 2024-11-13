package com.studleague.studleague.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        if (authException.getClass().getSimpleName().equals("BadCredentialsException")) {
            response.getWriter().write("{\"error\": \"Wrong login or password\"}");
        } else {
            response.getWriter().write("{\"error\": \"Access denied\"}");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }


}

