package com.qcm.backend.repository;

import com.qcm.backend.entity.ReponsePossible;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReponsePossibleRepository extends JpaRepository<ReponsePossible, Long> {
    List<ReponsePossible> findByQuestionId(Long questionId);
}