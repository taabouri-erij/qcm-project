package com.qcm.backend.dto;

public class EvaluationQuestionDTO {
    private Long id;
    private Double points;
    private Integer ordre;
    private Long evaluationId;
    private QuestionDTO question;

    public EvaluationQuestionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getPoints() { return points; }
    public void setPoints(Double points) { this.points = points; }
    public Integer getOrdre() { return ordre; }
    public void setOrdre(Integer ordre) { this.ordre = ordre; }
    public Long getEvaluationId() { return evaluationId; }
    public void setEvaluationId(Long evaluationId) { this.evaluationId = evaluationId; }
    public QuestionDTO getQuestion() { return question; }
    public void setQuestion(QuestionDTO question) { this.question = question; }
}