package com.qcm.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "alertes")
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type; // CHANGEMENT_ONGLET, SORTIE_PLEIN_ECRAN, etc.

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure = LocalDateTime.now();

    private Integer gravite = 1;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "tentative_id", nullable = false)
    private Tentative tentative;

    // NULLABLE → alerte générale si null
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Alerte() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public Integer getGravite() { return gravite; }
    public void setGravite(Integer gravite) { this.gravite = gravite; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Tentative getTentative() { return tentative; }
    public void setTentative(Tentative tentative) { this.tentative = tentative; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
}