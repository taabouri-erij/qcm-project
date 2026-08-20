package com.qcm.backend.repository;

import com.qcm.backend.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    List<Matiere> findByModuleId(Long moduleId);
}