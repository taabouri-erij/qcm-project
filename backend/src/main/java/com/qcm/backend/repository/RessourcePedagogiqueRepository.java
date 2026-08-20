package com.qcm.backend.repository;

import com.qcm.backend.entity.RessourcePedagogique;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RessourcePedagogiqueRepository extends JpaRepository<RessourcePedagogique, Long> {
    List<RessourcePedagogique> findByChapitreId(Long chapitreId);
}