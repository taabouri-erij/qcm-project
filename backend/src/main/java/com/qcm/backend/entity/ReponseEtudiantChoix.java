package com.qcm.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "reponses_etudiant_choix")
public class ReponseEtudiantChoix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reponse_etudiant_id", nullable = false)
    private ReponseEtudiant reponseEtudiant;

    @ManyToOne
    @JoinColumn(name = "reponse_possible_id", nullable = false)
    private ReponsePossible reponsePossible;

    public ReponseEtudiantChoix() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ReponseEtudiant getReponseEtudiant() { return reponseEtudiant; }
    public void setReponseEtudiant(ReponseEtudiant reponseEtudiant) { this.reponseEtudiant = reponseEtudiant; }

    public ReponsePossible getReponsePossible() { return reponsePossible; }
    public void setReponsePossible(ReponsePossible reponsePossible) { this.reponsePossible = reponsePossible; }
}