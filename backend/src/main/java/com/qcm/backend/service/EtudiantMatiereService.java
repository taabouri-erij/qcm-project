package com.qcm.backend.service;

import com.qcm.backend.entity.EtudiantMatiere;
import com.qcm.backend.entity.Matiere;
import com.qcm.backend.entity.User;
import com.qcm.backend.repository.EtudiantMatiereRepository;
import com.qcm.backend.repository.MatiereRepository;
import com.qcm.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtudiantMatiereService {

    private final EtudiantMatiereRepository etudiantMatiereRepository;
    private final UserRepository userRepository;
    private final MatiereRepository matiereRepository;

    public EtudiantMatiereService(EtudiantMatiereRepository etudiantMatiereRepository,
                                  UserRepository userRepository,
                                  MatiereRepository matiereRepository) {
        this.etudiantMatiereRepository = etudiantMatiereRepository;
        this.userRepository = userRepository;
        this.matiereRepository = matiereRepository;
    }

    public List<EtudiantMatiere> getByEtudiant(Long etudiantId) {
        return etudiantMatiereRepository.findByEtudiantId(etudiantId);
    }

    public List<EtudiantMatiere> getByMatiere(Long matiereId) {
        return etudiantMatiereRepository.findByMatiereId(matiereId);
    }

    public EtudiantMatiere associer(Long etudiantId, Long matiereId) {
        User etudiant = userRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));

        EtudiantMatiere em = new EtudiantMatiere();
        em.setEtudiant(etudiant);
        em.setMatiere(matiere);
        return etudiantMatiereRepository.save(em);
    }

    public void dissocier(Long id) {
        etudiantMatiereRepository.deleteById(id);
    }
}