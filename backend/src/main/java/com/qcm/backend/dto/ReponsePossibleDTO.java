package com.qcm.backend.dto;

public class ReponsePossibleDTO {
    private Long id;
    private String texte;
    private Boolean estCorrecte;
    private Integer ordre;
    private Long questionId;

    public ReponsePossibleDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }
    public Boolean getEstCorrecte() { return estCorrecte; }
    public void setEstCorrecte(Boolean estCorrecte) { this.estCorrecte = estCorrecte; }
    public Integer getOrdre() { return ordre; }
    public void setOrdre(Integer ordre) { this.ordre = ordre; }
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
}