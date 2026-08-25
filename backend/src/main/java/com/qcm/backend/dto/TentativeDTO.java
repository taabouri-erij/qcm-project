package com.qcm.backend.dto;

import java.time.LocalDateTime;

public class TentativeDTO {

    private Long id;
    private String statut;
    private Double score;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer nombreAlertes;

    // Infos de l'étudiant, juste ce qu'il faut (pas l'entité User entière)
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantPrenom;

    // Infos de l'évaluation, juste ce qu'il faut (pas l'entité Evaluation entière)
    private Long evaluationId;
    private String evaluationTitre;

    // Constructeur vide (nécessaire pour certains frameworks)
    public TentativeDTO() {
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }

    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }

    public Integer getNombreAlertes() { return nombreAlertes; }
    public void setNombreAlertes(Integer nombreAlertes) { this.nombreAlertes = nombreAlertes; }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }

    public String getEtudiantNom() { return etudiantNom; }
    public void setEtudiantNom(String etudiantNom) { this.etudiantNom = etudiantNom; }

    public String getEtudiantPrenom() { return etudiantPrenom; }
    public void setEtudiantPrenom(String etudiantPrenom) { this.etudiantPrenom = etudiantPrenom; }

    public Long getEvaluationId() { return evaluationId; }
    public void setEvaluationId(Long evaluationId) { this.evaluationId = evaluationId; }

    public String getEvaluationTitre() { return evaluationTitre; }
    public void setEvaluationTitre(String evaluationTitre) { this.evaluationTitre = evaluationTitre; }
}