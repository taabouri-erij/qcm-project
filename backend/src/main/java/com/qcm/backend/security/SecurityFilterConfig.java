package com.qcm.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityFilterConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("!!!!! SECURITY FILTER CHAIN CHARGEE !!!!!");

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/modules/**", "/api/matieres/**",
                                "/api/chapitres/**", "/api/questions/**", "/api/evaluations/**",
                                "/api/evaluation-questions/**", "/api/reponses-possibles/**")
                        .hasAnyRole("ENSEIGNANT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/modules/**", "/api/matieres/**",
                                "/api/chapitres/**", "/api/questions/**", "/api/evaluations/**",
                                "/api/evaluation-questions/**", "/api/reponses-possibles/**")
                        .hasAnyRole("ENSEIGNANT", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/modules/**", "/api/matieres/**",
                                "/api/chapitres/**", "/api/questions/**", "/api/evaluations/**",
                                "/api/evaluation-questions/**", "/api/reponses-possibles/**")
                        .hasAnyRole("ENSEIGNANT", "ADMIN")
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}