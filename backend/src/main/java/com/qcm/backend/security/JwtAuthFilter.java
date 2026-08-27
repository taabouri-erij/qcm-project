package com.qcm.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        // Pas de header, ou ne commence pas par "Bearer " -> on laisse passer sans authentifier
        // (Spring Security décidera plus tard si cet endpoint nécessite d'être connecté)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // enlève "Bearer " (7 caractères)

        if (jwtUtil.estValide(token)) {
            String email = jwtUtil.extraireEmail(token);
            String role = jwtUtil.extraireRole(token);
            Long userId = jwtUtil.extraireUserId(token);

            // On crée un objet "Authentication" que Spring Security comprend.
            // Le rôle doit être préfixé par "ROLE_" par convention Spring Security.
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            var authentication = new UsernamePasswordAuthenticationToken(
                    email,          // "principal" : qui est l'utilisateur (ici son email)
                    null,           // pas besoin du mot de passe ici, déjà vérifié au login
                    authorities     // ses droits (son rôle)
            );

            // On attache aussi le userId pour pouvoir le récupérer facilement dans les controllers
            authentication.setDetails(userId);

            // On enregistre cette authentification dans le contexte de sécurité de Spring
            // -> c'est CE qui rend l'utilisateur "connu" pour le reste du traitement de la requête
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Dans tous les cas, on laisse la requête continuer son chemin
        filterChain.doFilter(request, response);
    }
}