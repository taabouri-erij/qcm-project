package com.qcm.backend.service;

import com.qcm.backend.entity.Question;
import com.qcm.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> getQuestionsByChapitre(Long chapitreId) {
        return questionRepository.findByChapitreId(chapitreId);
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question details) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question non trouvée"));
        question.setEnonce(details.getEnonce());
        question.setType(details.getType());
        question.setDifficulte(details.getDifficulte());
        question.setPointsDefaut(details.getPointsDefaut());
        question.setChapitre(details.getChapitre());
        return questionRepository.save(question);
    }

    // Dupliquer une question
    public Question dupliquer(Long id) {
        Question originale = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question non trouvée"));

        Question copie = new Question();
        copie.setEnonce(originale.getEnonce() + " (copie)");
        copie.setType(originale.getType());
        copie.setDifficulte(originale.getDifficulte());
        copie.setPointsDefaut(originale.getPointsDefaut());
        copie.setChapitre(originale.getChapitre());

        return questionRepository.save(copie);
    }
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}