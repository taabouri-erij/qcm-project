package com.qcm.backend.dto;

public class LoginResponse {
    private String token;
    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private String role;

    public LoginResponse() {}

    public LoginResponse(String token, Long userId, String nom, String prenom, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}