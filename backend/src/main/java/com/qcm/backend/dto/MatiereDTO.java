package com.qcm.backend.dto;

public class MatiereDTO {
    private Long id;
    private String nom;
    private String description;
    private Long moduleId;
    private String moduleNom;

    public MatiereDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    public String getModuleNom() { return moduleNom; }
    public void setModuleNom(String moduleNom) { this.moduleNom = moduleNom; }
}