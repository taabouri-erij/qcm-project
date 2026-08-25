package com.qcm.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EvaluationDTO {
    private Long id;
    private String titre;
    private String type;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer dureeMinutes;
    private Integer nombreTentativesMax;
    private Boolean ordreAleatoire;
    private Boolean publie;
    private Long chapitreId;
    private String chapitreTitre;
    private Long matiereId;
    private String matiereNom;
    private List<EvaluationQuestionDTO> questions;

    public EvaluationDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    public Integer getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(Integer dureeMinutes) { this.dureeMinutes = dureeMinutes; }
    public Integer getNombreTentativesMax() { return nombreTentativesMax; }
    public void setNombreTentativesMax(Integer nombreTentativesMax) { this.nombreTentativesMax = nombreTentativesMax; }
    public Boolean getOrdreAleatoire() { return ordreAleatoire; }
    public void setOrdreAleatoire(Boolean ordreAleatoire) { this.ordreAleatoire = ordreAleatoire; }
    public Boolean getPublie() { return publie; }
    public void setPublie(Boolean publie) { this.publie = publie; }
    public Long getChapitreId() { return chapitreId; }
    public void setChapitreId(Long chapitreId) { this.chapitreId = chapitreId; }
    public String getChapitreTitre() { return chapitreTitre; }
    public void setChapitreTitre(String chapitreTitre) { this.chapitreTitre = chapitreTitre; }
    public Long getMatiereId() { return matiereId; }
    public void setMatiereId(Long matiereId) { this.matiereId = matiereId; }
    public String getMatiereNom() { return matiereNom; }
    public void setMatiereNom(String matiereNom) { this.matiereNom = matiereNom; }
    public List<EvaluationQuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<EvaluationQuestionDTO> questions) { this.questions = questions; }
}