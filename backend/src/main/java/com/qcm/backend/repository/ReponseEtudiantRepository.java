package com.qcm.backend.repository;

import com.qcm.backend.entity.ReponseEtudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReponseEtudiantRepository extends JpaRepository<ReponseEtudiant, Long> {
    List<ReponseEtudiant> findByTentativeId(Long tentativeId);
}