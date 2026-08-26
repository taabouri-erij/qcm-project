package com.qcm.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RessourceNonTrouveeException.class)
    public ResponseEntity<Map<String, Object>> gererNonTrouve(RessourceNonTrouveeException e) {
        return construireReponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflitException.class)
    public ResponseEntity<Map<String, Object>> gererConflit(ConflitException e) {
        return construireReponse(HttpStatus.CONFLICT, e.getMessage());
    }

    // Filet de sécurité : toute autre RuntimeException non catégorisée
    // (ex: règles métier comme "cette tentative n'est plus modifiable")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> gererErreurGenerique(RuntimeException e) {
        return construireReponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<Map<String, Object>> construireReponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}