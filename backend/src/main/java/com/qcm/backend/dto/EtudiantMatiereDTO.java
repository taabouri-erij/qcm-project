package com.qcm.backend.dto;

public class EtudiantMatiereDTO {
    private Long id;
    private Long matiereId;
    private String matiereNom;
    private Long etudiantId;

    public EtudiantMatiereDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMatiereId() { return matiereId; }
    public void setMatiereId(Long matiereId) { this.matiereId = matiereId; }
    public String getMatiereNom() { return matiereNom; }
    public void setMatiereNom(String matiereNom) { this.matiereNom = matiereNom; }
    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }
}