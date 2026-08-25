package com.qcm.backend.controller;

import com.qcm.backend.dto.QuestionDTO;
import com.qcm.backend.entity.Question;
import com.qcm.backend.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<QuestionDTO> getAllQuestions() {
        return questionService.getAllQuestions().stream().map(questionService::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(questionService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/chapitre/{chapitreId}")
    public List<QuestionDTO> getQuestionsByChapitre(@PathVariable Long chapitreId) {
        return questionService.getQuestionsByChapitre(chapitreId).stream().map(questionService::convertToDTO).toList();
    }

    @PostMapping
    public QuestionDTO createQuestion(@RequestBody Question question) {
        return questionService.convertToDTO(questionService.createQuestion(question));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody Question details) {
        try {
            return ResponseEntity.ok(questionService.convertToDTO(questionService.updateQuestion(id, details)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/dupliquer")
    public ResponseEntity<QuestionDTO> dupliquer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(questionService.convertToDTO(questionService.dupliquer(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}