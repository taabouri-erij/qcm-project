package com.qcm.backend.repository;

import com.qcm.backend.entity.EtudiantMatiere;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EtudiantMatiereRepository extends JpaRepository<EtudiantMatiere, Long> {
    List<EtudiantMatiere> findByEtudiantId(Long etudiantId);
    List<EtudiantMatiere> findByMatiereId(Long matiereId);
}