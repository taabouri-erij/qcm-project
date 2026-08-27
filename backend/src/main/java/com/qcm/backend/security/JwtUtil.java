package com.qcm.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // La clé est maintenant lue depuis application.properties (jwt.secret),
    // au lieu d'être générée aléatoirement à chaque démarrage.
    private final SecretKey secretKey;

    private final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    public JwtUtil(@Value("${jwt.secret}") String secretBase64) {
        this.secretKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(secretBase64));
    }

    public String genererToken(Long userId, String email, String role) {
        Date maintenant = new Date();
        Date expiration = new Date(maintenant.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(maintenant)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public Claims extraireClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extraireEmail(String token) {
        return extraireClaims(token).getSubject();
    }

    public Long extraireUserId(String token) {
        return extraireClaims(token).get("userId", Long.class);
    }

    public String extraireRole(String token) {
        return extraireClaims(token).get("role", String.class);
    }

    public boolean estValide(String token) {
        try {
            Claims claims = extraireClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}