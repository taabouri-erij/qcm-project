package com.qcm.backend.repository;

import com.qcm.backend.entity.Chapitre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChapitreRepository extends JpaRepository<Chapitre, Long> {
    List<Chapitre> findByMatiereId(Long matiereId);
}