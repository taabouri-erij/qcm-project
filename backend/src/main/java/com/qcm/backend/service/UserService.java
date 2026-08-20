package com.qcm.backend.service;

import com.qcm.backend.entity.User;
import com.qcm.backend.repository.TentativeRepository;
import com.qcm.backend.repository.UserRepository;
import com.qcm.backend.repository.EnseignantMatiereRepository;
import com.qcm.backend.repository.EtudiantMatiereRepository;
import com.qcm.backend.repository.AuditLogRepository;
import com.qcm.backend.entity.AuditLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TentativeRepository tentativeRepository;
    private final EnseignantMatiereRepository enseignantMatiereRepository;
    private final EtudiantMatiereRepository etudiantMatiereRepository;
    private final AuditLogRepository auditLogRepository;

    public UserService(UserRepository userRepository,
                       TentativeRepository tentativeRepository,
                       EnseignantMatiereRepository enseignantMatiereRepository,
                       EtudiantMatiereRepository etudiantMatiereRepository,
                       AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.tentativeRepository = tentativeRepository;
        this.enseignantMatiereRepository = enseignantMatiereRepository;
        this.etudiantMatiereRepository = etudiantMatiereRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Cet email existe déjà");
        }
        User saved = userRepository.save(user);
        enregistrerAudit("CREATION_USER", "User", saved.getId(),
                "Création de l'utilisateur " + saved.getEmail(), null);
        return saved;
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setActif(userDetails.getActif());

        User saved = userRepository.save(user);
        enregistrerAudit("MODIFICATION_USER", "User", saved.getId(),
                "Modification de l'utilisateur " + saved.getEmail(), null);
        return saved;
    }

    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActif(true);
        userRepository.save(user);
        enregistrerAudit("ACTIVATION_USER", "User", id, "Activation du compte", null);
    }

    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActif(false);
        userRepository.save(user);
        enregistrerAudit("DESACTIVATION_USER", "User", id, "Désactivation du compte", null);
    }

    @Transactional
    public void resetPassword(Long id, String nouveauMotDePasse) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setPassword(nouveauMotDePasse);
        userRepository.save(user);
        enregistrerAudit("RESET_PASSWORD", "User", id, "Réinitialisation du mot de passe", null);
    }

    public List<User> searchUsers(String nom, String role, Boolean actif) {
        return userRepository.findAll().stream()
                .filter(u -> nom == null || nom.isBlank()
                        || u.getNom().toLowerCase().contains(nom.toLowerCase())
                        || u.getPrenom().toLowerCase().contains(nom.toLowerCase()))
                .filter(u -> role == null || role.isBlank() || u.getRole().equalsIgnoreCase(role))
                .filter(u -> actif == null || u.getActif().equals(actif))
                .collect(Collectors.toList());
    }

    /**
     * Suppression protégée :
     * - Si l'utilisateur a des données liées (tentatives, associations) → désactivation
     * - Sinon → suppression définitive
     */
    @Transactional
    public String deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        boolean aDesTentatives = !tentativeRepository.findByEtudiantId(id).isEmpty();
        boolean aDesAssociationsEnseignant = !enseignantMatiereRepository.findByEnseignantId(id).isEmpty();
        boolean aDesAssociationsEtudiant = !etudiantMatiereRepository.findByEtudiantId(id).isEmpty();

        if (aDesTentatives || aDesAssociationsEnseignant || aDesAssociationsEtudiant) {
            user.setActif(false);
            userRepository.save(user);
            enregistrerAudit("DESACTIVATION_USER", "User", id,
                    "Suppression refusée (données liées) → compte désactivé", null);
            return "DESACTIVE"; // le compte a été désactivé
        }

        userRepository.deleteById(id);
        enregistrerAudit("SUPPRESSION_USER", "User", id,
                "Suppression définitive de l'utilisateur", null);
        return "SUPPRIME";
    }

    private void enregistrerAudit(String action, String entite, Long entiteId,
                                  String description, User utilisateur) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntite(entite);
        log.setEntiteId(entiteId);
        log.setDescription(description);
        log.setUtilisateur(utilisateur);
        auditLogRepository.save(log);
    }
}