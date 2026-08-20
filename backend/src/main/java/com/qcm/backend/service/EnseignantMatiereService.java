package com.qcm.backend.service;

import com.qcm.backend.entity.EnseignantMatiere;
import com.qcm.backend.entity.Matiere;
import com.qcm.backend.entity.User;
import com.qcm.backend.repository.EnseignantMatiereRepository;
import com.qcm.backend.repository.MatiereRepository;
import com.qcm.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnseignantMatiereService {

    private final EnseignantMatiereRepository enseignantMatiereRepository;
    private final UserRepository userRepository;
    private final MatiereRepository matiereRepository;

    public EnseignantMatiereService(EnseignantMatiereRepository enseignantMatiereRepository,
                                    UserRepository userRepository,
                                    MatiereRepository matiereRepository) {
        this.enseignantMatiereRepository = enseignantMatiereRepository;
        this.userRepository = userRepository;
        this.matiereRepository = matiereRepository;
    }

    public List<EnseignantMatiere> getByEnseignant(Long enseignantId) {
        return enseignantMatiereRepository.findByEnseignantId(enseignantId);
    }

    public List<EnseignantMatiere> getByMatiere(Long matiereId) {
        return enseignantMatiereRepository.findByMatiereId(matiereId);
    }

    public EnseignantMatiere associer(Long enseignantId, Long matiereId) {
        User enseignant = userRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));

        EnseignantMatiere em = new EnseignantMatiere();
        em.setEnseignant(enseignant);
        em.setMatiere(matiere);
        return enseignantMatiereRepository.save(em);
    }

    public void dissocier(Long id) {
        enseignantMatiereRepository.deleteById(id);
    }
}