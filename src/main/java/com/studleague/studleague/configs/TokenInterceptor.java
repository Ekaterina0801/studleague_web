package com.studleague.studleague.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final String TOKEN_ATTRIBUTE = "token";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = (String) request.getSession().getAttribute(TOKEN_ATTRIBUTE);

        if (token != null && !token.trim().isEmpty()) {
            request.setAttribute(AUTHORIZATION_HEADER, BEARER_PREFIX + token);
        }

        return true;
    }
}

