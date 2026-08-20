package com.qcm.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matieres")
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    @JsonIgnoreProperties("matieres")
    private Module module;

    @OneToMany(mappedBy = "matiere")
    @JsonIgnoreProperties("matiere")
    private List<Chapitre> chapitres = new ArrayList<>();

    public Matiere() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }

    public List<Chapitre> getChapitres() { return chapitres; }
    public void setChapitres(List<Chapitre> chapitres) { this.chapitres = chapitres; }
}