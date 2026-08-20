package com.qcm.backend.repository;

import com.qcm.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByChapitreId(Long chapitreId);
    List<Question> findByChapitreIdAndDifficulte(Long chapitreId, String difficulte);
}