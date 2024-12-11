package com.studleague.studleague.configs;

import com.studleague.studleague.services.implementations.security.JwtService;
import com.studleague.studleague.services.implementations.security.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private static final String REFRESH_HEADER_NAME = "Refresh-Token";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        String[] excludedPaths = {
                "/results",
                "/teams",
                //"/tournaments",
                "/auth/**",
                "/leagues/**"
        };


        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return !path.startsWith("/api/leagues/") || !path.contains("/is-manager");
        }
        for (String excludedPath : excludedPaths) {
            if (path.startsWith(excludedPath.replace("**", ""))) {
                return true;
            }
        }

        return false;
    }




    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var authHeader = request.getHeader(HEADER_NAME);
        if ((StringUtils.isEmpty(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) &&
                !request.getRequestURL().toString().contains("/sign-in")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authorization token is missing");
            return;
        }

        if (request.getRequestURL().toString().contains("/sign-in")) {
            filterChain.doFilter(request, response);
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
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid access token");
            return;
        }

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.findUserByUsername(username);

            if (!tokenExpired && jwtService.isTokenValid(jwt, userDetails)) {
                authenticateUser(userDetails, request);
            } else if (tokenExpired) {
                String refreshToken = request.getHeader(REFRESH_HEADER_NAME);
                if (StringUtils.isEmpty(refreshToken)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing refresh token");
                    return;
                }

                try {
                    if (jwtService.isTokenValid(refreshToken, userDetails)) {
                        String newAccessToken = jwtService.generateAccessToken(userDetails);
                        response.setHeader("Authorization", "Bearer " + newAccessToken);
                        authenticateUser(userDetails, request);
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid refresh token");
                        return;
                    }
                } catch (JwtException e) {
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