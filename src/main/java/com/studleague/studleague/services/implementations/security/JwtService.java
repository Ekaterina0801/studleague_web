package com.studleague.studleague.services.implementations.security;

import com.studleague.studleague.entities.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /*private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 минут
    public static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 дней
    private static final long RESET_PASSWORD_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1 час
*/
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 10 * 10; // 100 минута
    public static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 2 * 10 * 10; // 200 минуты
    private static final long RESET_PASSWORD_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1 час

    // Извлечение имени пользователя из токена
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Генерация токена
    public String generateToken(User user, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return generateToken(claims, user, expiration);
    }

    // Проверка токена на валидность
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    // Генерация токена для сброса пароля
    public String generateResetPasswordToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        return generateToken(claims, user, RESET_PASSWORD_TOKEN_EXPIRATION);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    // Проверка токена сброса пароля на валидность
    public boolean validateResetPasswordToken(String token, String email) {
        String tokenEmail = extractClaim(token, Claims::getSubject);
        return tokenEmail.equals(email) && !isTokenExpired(token);
    }

    // Генерация access и refresh токенов
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Проверка токена на просроченность
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Извлечение даты истечения токена
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // Извлечение всех данных из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    // Получение ключа для подписи токена
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
