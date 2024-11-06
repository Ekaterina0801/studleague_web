package com.studleague.studleague.configs;

import com.studleague.studleague.services.implementations.security.JwtService;
import com.studleague.studleague.services.implementations.security.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private static final String REFRESH_HEADER_NAME = "Refresh-Token";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        var jwt = authHeader.substring(BEARER_PREFIX.length());
        String username = null;
        boolean tokenExpired = false;

        try {
            username = jwtService.extractUserName(jwt);
        } catch (ExpiredJwtException e) {
            tokenExpired = true;
            username = e.getClaims().getSubject();
        }

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.findUserByUsername(username);

            if (!tokenExpired && jwtService.isTokenValid(jwt, userDetails)) {
                // Если токен валиден и не истек, аутентифицируем пользователя
                authenticateUser(userDetails, request);
            } else if (tokenExpired) {
                String refreshToken = request.getHeader(REFRESH_HEADER_NAME);
                if (StringUtils.isEmpty(refreshToken)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing refresh token");
                    return;
                }

                // Проверяем валидность refresh токена
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    String newAccessToken = jwtService.generateAccessToken(userDetails);
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    authenticateUser(userDetails, request);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid refresh token");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }





}