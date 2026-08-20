package com.qcm.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tentatives")
public class Tentative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonIgnoreProperties({"password"})
    private User etudiant;

    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    @JsonIgnoreProperties({"evaluationQuestions"})
    private Evaluation evaluation;

    @Column(nullable = false, length = 20)
    private String statut = "EN_COURS"; // EN_COURS, SOUMIS, EXPIRE, ANNULE

    private LocalDateTime dateDebut = LocalDateTime.now();
    private LocalDateTime dateFin;
    private Double score;
    private Integer nombreAlertes = 0;

    @OneToMany(mappedBy = "tentative")
    @JsonIgnoreProperties("tentative")
    private List<ReponseEtudiant> reponsesEtudiant = new ArrayList<>();

    @OneToMany(mappedBy = "tentative")
    @JsonIgnoreProperties("tentative")
    private List<Alerte> alertes = new ArrayList<>();

    public Tentative() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(User etudiant) {
        this.etudiant = etudiant;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getNombreAlertes() {
        return nombreAlertes;
    }

    public void setNombreAlertes(Integer nombreAlertes) {
        this.nombreAlertes = nombreAlertes;
    }

    public List<ReponseEtudiant> getReponsesEtudiant() {
        return reponsesEtudiant;
    }

    public void setReponsesEtudiant(List<ReponseEtudiant> reponsesEtudiant) {
        this.reponsesEtudiant = reponsesEtudiant;
    }

    public List<Alerte> getAlertes() {
        return alertes;
    }

    public void setAlertes(List<Alerte> alertes) {
        this.alertes = alertes;
    }
}