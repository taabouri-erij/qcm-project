package com.qcm.backend.service;

import com.qcm.backend.entity.Evaluation;
import com.qcm.backend.entity.EvaluationQuestion;
import com.qcm.backend.entity.Question;
import com.qcm.backend.repository.EvaluationQuestionRepository;
import com.qcm.backend.repository.EvaluationRepository;
import com.qcm.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationQuestionService {

    private final EvaluationQuestionRepository repository;
    private final EvaluationRepository evaluationRepository;
    private final QuestionRepository questionRepository;

    public EvaluationQuestionService(EvaluationQuestionRepository repository,
                                     EvaluationRepository evaluationRepository,
                                     QuestionRepository questionRepository) {
        this.repository = repository;
        this.evaluationRepository = evaluationRepository;
        this.questionRepository = questionRepository;
    }

    public List<EvaluationQuestion> getByEvaluation(Long evaluationId) {
        return repository.findByEvaluationId(evaluationId);
    }

    public EvaluationQuestion ajouterQuestion(Long evaluationId, Long questionId, Double points, Integer ordre) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question non trouvée"));

        EvaluationQuestion eq = new EvaluationQuestion();
        eq.setEvaluation(evaluation);
        eq.setQuestion(question);
        eq.setPoints(points != null ? points : question.getPointsDefaut());
        eq.setOrdre(ordre);
        return repository.save(eq);
    }

    public EvaluationQuestion update(Long id, Double points, Integer ordre) {
        EvaluationQuestion eq = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lien non trouvé"));
        if (points != null) eq.setPoints(points);
        if (ordre != null) eq.setOrdre(ordre);
        return repository.save(eq);
    }

    public void supprimer(Long id) {
        repository.deleteById(id);
    }
}