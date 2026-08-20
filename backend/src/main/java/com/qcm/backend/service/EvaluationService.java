package com.qcm.backend.service;

import com.qcm.backend.entity.Evaluation;
import com.qcm.backend.repository.EvaluationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    public List<Evaluation> getEvaluationsByChapitre(Long chapitreId) {
        return evaluationRepository.findByChapitreId(chapitreId);
    }

    public List<Evaluation> getEvaluationsByMatiere(Long matiereId) {
        return evaluationRepository.findByMatiereId(matiereId);
    }

    public Evaluation createEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    public Evaluation updateEvaluation(Long id, Evaluation details) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));
        evaluation.setTitre(details.getTitre());
        evaluation.setType(details.getType());
        evaluation.setDateDebut(details.getDateDebut());
        evaluation.setDateFin(details.getDateFin());
        evaluation.setDureeMinutes(details.getDureeMinutes());
        evaluation.setNombreTentativesMax(details.getNombreTentativesMax());
        evaluation.setOrdreAleatoire(details.getOrdreAleatoire());
        evaluation.setPublie(details.getPublie());
        return evaluationRepository.save(evaluation);
    }

    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }
}