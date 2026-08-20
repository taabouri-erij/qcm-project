package com.qcm.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enonce;

    @Column(nullable = false, length = 20)
    private String type; // CHOIX_UNIQUE, CHOIX_MULTIPLE

    @Column(nullable = false, length = 20)
    private String difficulte; // FACILE, MOYEN, DIFFICILE

    private Double pointsDefaut = 1.0;

    @ManyToOne
    @JoinColumn(name = "chapitre_id", nullable = false)
    @JsonIgnoreProperties({"questions", "matiere"})
    private Chapitre chapitre;

    @OneToMany(mappedBy = "question")
    @JsonIgnoreProperties("question")
    private List<ReponsePossible> reponsesPossibles = new ArrayList<>();

    public Question() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnonce() { return enonce; }
    public void setEnonce(String enonce) { this.enonce = enonce; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDifficulte() { return difficulte; }
    public void setDifficulte(String difficulte) { this.difficulte = difficulte; }

    public Double getPointsDefaut() { return pointsDefaut; }
    public void setPointsDefaut(Double pointsDefaut) { this.pointsDefaut = pointsDefaut; }

    public Chapitre getChapitre() { return chapitre; }
    public void setChapitre(Chapitre chapitre) { this.chapitre = chapitre; }

    public List<ReponsePossible> getReponsesPossibles() { return reponsesPossibles; }
    public void setReponsesPossibles(List<ReponsePossible> reponsesPossibles) {
        this.reponsesPossibles = reponsesPossibles;
    }
}