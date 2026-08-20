package com.qcm.backend.service;

import com.qcm.backend.entity.*;
import com.qcm.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    private final EtudiantMatiereRepository etudiantMatiereRepository;
    private final EvaluationRepository evaluationRepository;
    private final ChapitreRepository chapitreRepository;
    private final TentativeService tentativeService;

    public EtudiantService(EtudiantMatiereRepository etudiantMatiereRepository,
                           EvaluationRepository evaluationRepository,
                           ChapitreRepository chapitreRepository,
                           TentativeService tentativeService) {
        this.etudiantMatiereRepository = etudiantMatiereRepository;
        this.evaluationRepository = evaluationRepository;
        this.chapitreRepository = chapitreRepository;
        this.tentativeService = tentativeService;
    }

    // Matières de l'étudiant
    public List<Matiere> getMatieres(Long etudiantId) {
        return etudiantMatiereRepository.findByEtudiantId(etudiantId).stream()
                .map(EtudiantMatiere::getMatiere)
                .collect(Collectors.toList());
    }

    // Évaluations disponibles pour l'étudiant (publiées, de ses matières)
    public List<Evaluation> getEvaluationsDisponibles(Long etudiantId) {
        List<Matiere> matieres = getMatieres(etudiantId);
        List<Long> matiereIds = matieres.stream().map(Matiere::getId).collect(Collectors.toList());

        List<Evaluation> result = new ArrayList<>();

        for (Evaluation e : evaluationRepository.findAll()) {
            if (!Boolean.TRUE.equals(e.getPublie())) continue;

            // Examen final rattaché à une matière
            if (e.getMatiere() != null && matiereIds.contains(e.getMatiere().getId())) {
                result.add(e);
                continue;
            }

            // Test / Examen rattaché à un chapitre d'une de ses matières
            if (e.getChapitre() != null
                    && e.getChapitre().getMatiere() != null
                    && matiereIds.contains(e.getChapitre().getMatiere().getId())) {
                result.add(e);
            }
        }

        return result;
    }
}