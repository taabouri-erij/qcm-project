package com.qcm.backend.repository;

import com.qcm.backend.entity.Tentative;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TentativeRepository extends JpaRepository<Tentative, Long> {
    List<Tentative> findByEtudiantId(Long etudiantId);
    List<Tentative> findByEvaluationId(Long evaluationId);
    List<Tentative> findByEtudiantIdAndEvaluationId(Long etudiantId, Long evaluationId);
}