package com.qcm.backend.repository;

import com.qcm.backend.entity.EvaluationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationQuestionRepository extends JpaRepository<EvaluationQuestion, Long> {
    List<EvaluationQuestion> findByEvaluationId(Long evaluationId);
}