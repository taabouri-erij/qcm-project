package com.qcm.backend.dto;

import java.util.List;

public class QuestionDTO {
    private Long id;
    private String enonce;
    private String type;
    private String difficulte;
    private Double pointsDefaut;
    private Long chapitreId;
    private String chapitreTitre;
    private List<ReponsePossibleDTO> reponsesPossibles;

    public QuestionDTO() {}

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
    public Long getChapitreId() { return chapitreId; }
    public void setChapitreId(Long chapitreId) { this.chapitreId = chapitreId; }
    public String getChapitreTitre() { return chapitreTitre; }
    public void setChapitreTitre(String chapitreTitre) { this.chapitreTitre = chapitreTitre; }
    public List<ReponsePossibleDTO> getReponsesPossibles() { return reponsesPossibles; }
    public void setReponsesPossibles(List<ReponsePossibleDTO> reponsesPossibles) { this.reponsesPossibles = reponsesPossibles; }
}