package com.studleague.studleague.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Извлекаем токен из сессии
        String token = (String) request.getSession().getAttribute("token");

        // Если токен доступен, добавляем его в заголовок запроса
        if (token != null && !token.isEmpty()) {
            request.setAttribute("Authorization", "Bearer " + token);
        }
        return true;  // Продолжаем выполнение запроса
    }
}

