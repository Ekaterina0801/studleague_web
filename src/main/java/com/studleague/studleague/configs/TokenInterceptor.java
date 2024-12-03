package com.studleague.studleague.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = (String) request.getSession().getAttribute("token");

        if (token != null && !token.isEmpty()) {
            request.setAttribute("Authorization", "Bearer " + token);
        }
        return true;
    }
}

