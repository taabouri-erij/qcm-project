package com.qcm.backend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "reponses_etudiant")
public class ReponseEtudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score_question")
    private Double scoreQuestion;

    @ManyToOne
    @JoinColumn(name = "tentative_id", nullable = false)
    private Tentative tentative;

    @ManyToOne
    @JoinColumn(name = "evaluation_question_id", nullable = false)
    private EvaluationQuestion evaluationQuestion;

    @OneToMany(mappedBy = "reponseEtudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReponseEtudiantChoix> choix = new ArrayList<>();

    public ReponseEtudiant() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getScoreQuestion() { return scoreQuestion; }
    public void setScoreQuestion(Double scoreQuestion) { this.scoreQuestion = scoreQuestion; }

    public Tentative getTentative() { return tentative; }
    public void setTentative(Tentative tentative) { this.tentative = tentative; }

    public EvaluationQuestion getEvaluationQuestion() { return evaluationQuestion; }
    public void setEvaluationQuestion(EvaluationQuestion evaluationQuestion) { this.evaluationQuestion = evaluationQuestion; }

    public List<ReponseEtudiantChoix> getChoix() { return choix; }
    public void setChoix(List<ReponseEtudiantChoix> choix) { this.choix = choix; }
}