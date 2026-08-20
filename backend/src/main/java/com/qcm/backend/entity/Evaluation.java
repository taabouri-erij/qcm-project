package com.qcm.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(nullable = false, length = 30)
    private String type;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer dureeMinutes;
    private Integer nombreTentativesMax = 1;
    private Boolean ordreAleatoire = true;
    private Boolean publie = false;

    @ManyToOne
    @JoinColumn(name = "chapitre_id")
    @JsonIgnoreProperties({"questions", "matiere"})
    private Chapitre chapitre;

    @ManyToOne
    @JoinColumn(name = "matiere_id")
    @JsonIgnoreProperties({"chapitres", "module"})
    private Matiere matiere;

    @OneToMany(mappedBy = "evaluation")
    @JsonIgnoreProperties("evaluation")
    private List<EvaluationQuestion> evaluationQuestions = new ArrayList<>();

    public Evaluation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Integer dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public Integer getNombreTentativesMax() {
        return nombreTentativesMax;
    }

    public void setNombreTentativesMax(Integer nombreTentativesMax) {
        this.nombreTentativesMax = nombreTentativesMax;
    }

    public Boolean getOrdreAleatoire() {
        return ordreAleatoire;
    }

    public void setOrdreAleatoire(Boolean ordreAleatoire) {
        this.ordreAleatoire = ordreAleatoire;
    }

    public Boolean getPublie() {
        return publie;
    }

    public void setPublie(Boolean publie) {
        this.publie = publie;
    }

    public Chapitre getChapitre() {
        return chapitre;
    }

    public void setChapitre(Chapitre chapitre) {
        this.chapitre = chapitre;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public List<EvaluationQuestion> getEvaluationQuestions() {
        return evaluationQuestions;
    }

    public void setEvaluationQuestions(List<EvaluationQuestion> evaluationQuestions) {
        this.evaluationQuestions = evaluationQuestions;
    }
}