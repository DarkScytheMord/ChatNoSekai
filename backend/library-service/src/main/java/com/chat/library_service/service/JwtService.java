package com.chat.library_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Long extractUserIdFromAuthorizationHeader(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        Claims claims = extractAllClaims(token);

        Object userIdClaim = claims.get("userId");

        if (userIdClaim == null) {
            throw new RuntimeException("El token no contiene userId");
        }

        if (userIdClaim instanceof Integer) {
            return ((Integer) userIdClaim).longValue();
        }

        if (userIdClaim instanceof Long) {
            return (Long) userIdClaim;
        }

        return Long.valueOf(userIdClaim.toString());
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no enviado o formato inválido");
        }

        return authorizationHeader.substring(7);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}