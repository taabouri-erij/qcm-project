package com.qcm.backend.service;

import com.qcm.backend.entity.User;
import com.qcm.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final MatiereRepository matiereRepository;
    private final EvaluationRepository evaluationRepository;
    private final TentativeRepository tentativeRepository;

    public DashboardService(UserRepository userRepository,
                            ModuleRepository moduleRepository,
                            MatiereRepository matiereRepository,
                            EvaluationRepository evaluationRepository,
                            TentativeRepository tentativeRepository) {
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
        this.matiereRepository = matiereRepository;
        this.evaluationRepository = evaluationRepository;
        this.tentativeRepository = tentativeRepository;
    }

    // =========================================================
    // TABLEAU DE BORD GLOBAL (compteurs)
    // =========================================================
    public Map<String, Object> getDashboard() {
        List<User> users = userRepository.findAll();

        long totalUsers = users.size();
        long nbEnseignants = users.stream().filter(u -> "ENSEIGNANT".equalsIgnoreCase(u.getRole())).count();
        long nbEtudiants = users.stream().filter(u -> "ETUDIANT".equalsIgnoreCase(u.getRole())).count();
        long nbAdmins = users.stream().filter(u -> "ADMIN".equalsIgnoreCase(u.getRole())).count();
        long nbActifs = users.stream().filter(u -> Boolean.TRUE.equals(u.getActif())).count();
        long nbInactifs = totalUsers - nbActifs;

        long nbModules = moduleRepository.count();
        long nbMatieres = matiereRepository.count();
        long nbEvaluations = evaluationRepository.count();
        long nbTentatives = tentativeRepository.count();

        long nbTentativesSoumises = tentativeRepository.findAll().stream()
                .filter(t -> "SOUMIS".equals(t.getStatut()))
                .count();

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUtilisateurs", totalUsers);
        dashboard.put("nbEnseignants", nbEnseignants);
        dashboard.put("nbEtudiants", nbEtudiants);
        dashboard.put("nbAdmins", nbAdmins);
        dashboard.put("nbComptesActifs", nbActifs);
        dashboard.put("nbComptesInactifs", nbInactifs);
        dashboard.put("nbModules", nbModules);
        dashboard.put("nbMatieres", nbMatieres);
        dashboard.put("nbEvaluations", nbEvaluations);
        dashboard.put("nbTentatives", nbTentatives);
        dashboard.put("nbTentativesSoumises", nbTentativesSoumises);

        return dashboard;
    }

    // =========================================================
    // STATISTIQUES GLOBALES
    // =========================================================
    public Map<String, Object> getStatistiques() {
        List<com.qcm.backend.entity.Tentative> soumises = tentativeRepository.findAll().stream()
                .filter(t -> "SOUMIS".equals(t.getStatut()) && t.getScore() != null)
                .collect(Collectors.toList());

        double tauxReussite = 0;
        double moyenneGenerale = 0;

        if (!soumises.isEmpty()) {
            moyenneGenerale = soumises.stream()
                    .mapToDouble(com.qcm.backend.entity.Tentative::getScore)
                    .average()
                    .orElse(0);

            long nbReussites = soumises.stream()
                    .filter(t -> t.getScore() >= 10.0) // seuil de réussite = 10/20
                    .count();

            tauxReussite = (nbReussites * 100.0) / soumises.size();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("moyenneGenerale", Math.round(moyenneGenerale * 100.0) / 100.0);
        stats.put("tauxReussite", Math.round(tauxReussite * 100.0) / 100.0);
        stats.put("nbTentativesNotees", soumises.size());

        return stats;
    }
}