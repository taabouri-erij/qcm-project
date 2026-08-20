package com.qcm.backend.repository;

import com.qcm.backend.entity.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    List<Alerte> findByTentativeId(Long tentativeId);
    long countByTentativeId(Long tentativeId);
}