package com.qcm.backend.repository;

import com.qcm.backend.entity.EnseignantMatiere;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnseignantMatiereRepository extends JpaRepository<EnseignantMatiere, Long> {
    List<EnseignantMatiere> findByEnseignantId(Long enseignantId);
    List<EnseignantMatiere> findByMatiereId(Long matiereId);
}