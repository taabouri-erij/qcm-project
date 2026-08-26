package com.qcm.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète utilisée pour signer les tokens.
    // Générée une seule fois au démarrage de l'application.
    // ⚠️ En production, elle doit venir d'une configuration externe (variable d'environnement),
    // pas être écrite en dur dans le code. On simplifie pour l'instant.
    private final SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    // Durée de validité d'un token : 24h ici
    private final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    // Génère un token pour un utilisateur donné
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

    // Extrait toutes les infos (claims) d'un token, en vérifiant sa signature au passage
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

    // Vérifie si le token est valide (signature correcte + pas expiré)
    public boolean estValide(String token) {
        try {
            Claims claims = extraireClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false; // signature invalide, token corrompu, ou expiré
        }
    }
}