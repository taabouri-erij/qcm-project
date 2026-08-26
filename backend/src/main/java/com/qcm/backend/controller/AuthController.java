package com.qcm.backend.controller;

import com.qcm.backend.dto.LoginRequest;
import com.qcm.backend.dto.LoginResponse;
import com.qcm.backend.entity.User;
import com.qcm.backend.repository.UserRepository;
import com.qcm.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }

        if (!user.getActif()) {
            return ResponseEntity.status(403).body("Ce compte est désactivé");
        }

        String token = jwtUtil.genererToken(user.getId(), user.getEmail(), user.getRole());

        LoginResponse response = new LoginResponse(
                token, user.getId(), user.getNom(), user.getPrenom(), user.getEmail(), user.getRole()
        );

        return ResponseEntity.ok(response);
    }
}