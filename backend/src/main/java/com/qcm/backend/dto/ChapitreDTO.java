package com.qcm.backend.dto;

public class ChapitreDTO {
    private Long id;
    private String titre;
    private Integer numero;
    private String description;
    private Long matiereId;
    private String matiereNom;

    public ChapitreDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getMatiereId() { return matiereId; }
    public void setMatiereId(Long matiereId) { this.matiereId = matiereId; }
    public String getMatiereNom() { return matiereNom; }
    public void setMatiereNom(String matiereNom) { this.matiereNom = matiereNom; }
}