package com.qcm.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // Erreurs de validation (@Valid) : on liste chaque champ en erreur avec son message
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> gererValidation(MethodArgumentNotValidException e) {
        Map<String, String> erreursParChamp = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(erreur ->
                erreursParChamp.put(erreur.getField(), erreur.getDefaultMessage())
        );

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation échouée");
        body.put("erreurs", erreursParChamp);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

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