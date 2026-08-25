package com.qcm.backend.dto;

import java.util.List;

public class ReponseEtudiantDTO {
    private Long id;
    private Double scoreQuestion;
    private Long tentativeId;
    private Long evaluationQuestionId;
    private String questionEnonce;
    private List<Long> reponsesChoisiesIds;

    public ReponseEtudiantDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getScoreQuestion() { return scoreQuestion; }
    public void setScoreQuestion(Double scoreQuestion) { this.scoreQuestion = scoreQuestion; }
    public Long getTentativeId() { return tentativeId; }
    public void setTentativeId(Long tentativeId) { this.tentativeId = tentativeId; }
    public Long getEvaluationQuestionId() { return evaluationQuestionId; }
    public void setEvaluationQuestionId(Long evaluationQuestionId) { this.evaluationQuestionId = evaluationQuestionId; }
    public String getQuestionEnonce() { return questionEnonce; }
    public void setQuestionEnonce(String questionEnonce) { this.questionEnonce = questionEnonce; }
    public List<Long> getReponsesChoisiesIds() { return reponsesChoisiesIds; }
    public void setReponsesChoisiesIds(List<Long> reponsesChoisiesIds) { this.reponsesChoisiesIds = reponsesChoisiesIds; }
}