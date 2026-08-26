package com.qcm.backend.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    // Récupère l'id de l'utilisateur actuellement connecté (extrait du token JWT)
    public static Long getUserIdConnecte() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        // On avait stocké le userId dans "details" au moment de créer l'authentification (JwtAuthFilter)
        return (Long) auth.getDetails();
    }

    // Récupère le rôle actuel (ex: "ETUDIANT", "ENSEIGNANT", "ADMIN")
    public static String getRoleConnecte() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) {
            return null;
        }
        // Les rôles sont stockés sous la forme "ROLE_ETUDIANT" -> on enlève le préfixe
        String authority = auth.getAuthorities().iterator().next().getAuthority();
        return authority.replace("ROLE_", "");
    }

    // Vrai si l'utilisateur connecté est ENSEIGNANT ou ADMIN (accès élargi légitime)
    public static boolean estEnseignantOuAdmin() {
        String role = getRoleConnecte();
        return "ENSEIGNANT".equals(role) || "ADMIN".equals(role);
    }
}