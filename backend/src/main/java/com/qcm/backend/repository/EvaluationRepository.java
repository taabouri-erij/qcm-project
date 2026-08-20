package com.qcm.backend.repository;

import com.qcm.backend.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByChapitreId(Long chapitreId);
    List<Evaluation> findByMatiereId(Long matiereId);
    List<Evaluation> findByType(String type);
}