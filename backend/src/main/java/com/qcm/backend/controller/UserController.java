package com.qcm.backend.controller;

import com.qcm.backend.dto.CreateUserRequest;
import com.qcm.backend.dto.UpdateUserRequest;
import com.qcm.backend.dto.UserDTO;
import com.qcm.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(userService::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.convertToDTO(userService.getUserById(id));
    }

    @GetMapping("/search")
    public List<UserDTO> searchUsers(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean actif) {
        return userService.searchUsers(nom, role, actif)
                .stream()
                .map(userService::convertToDTO)
                .toList();
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.convertToDTO(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.convertToDTO(userService.updateUser(id, request));
    }

    @PutMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

    @PutMapping("/{id}/activate")
    public void activateUser(@PathVariable Long id) {
        userService.activateUser(id);
    }

    @PutMapping("/{id}/reset-password")
    public void resetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        userService.resetPassword(id, body.get("password"));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteUser(@PathVariable Long id) {
        String resultat = userService.deleteUser(id);
        return Map.of("resultat", resultat);
    }
}