-- =====================================================
-- PLATEFORME QCM - SCHEMA COMPLET
-- =====================================================

-- 1. UTILISATEURS
CREATE TABLE users (
                       id              BIGSERIAL PRIMARY KEY,
                       nom             VARCHAR(100) NOT NULL,
                       prenom          VARCHAR(100) NOT NULL,
                       email           VARCHAR(150) NOT NULL UNIQUE,
                       password        VARCHAR(255) NOT NULL,
                       role            VARCHAR(20)  NOT NULL,
                       actif           BOOLEAN      NOT NULL DEFAULT TRUE,
                       date_creation   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. MODULES
CREATE TABLE modules (
                         id              BIGSERIAL PRIMARY KEY,
                         nom             VARCHAR(150) NOT NULL,
                         description     TEXT
);

-- 3. MATIERES
CREATE TABLE matieres (
                          id              BIGSERIAL PRIMARY KEY,
                          nom             VARCHAR(150) NOT NULL,
                          description     TEXT,
                          module_id       BIGINT NOT NULL,
                          CONSTRAINT fk_matiere_module FOREIGN KEY (module_id) REFERENCES modules(id)
);

-- 4. CHAPITRES
CREATE TABLE chapitres (
                           id              BIGSERIAL PRIMARY KEY,
                           titre           VARCHAR(200) NOT NULL,
                           numero          INTEGER,
                           description     TEXT,
                           matiere_id      BIGINT NOT NULL,
                           CONSTRAINT fk_chapitre_matiere FOREIGN KEY (matiere_id) REFERENCES matieres(id)
);

-- 5. RESSOURCES PEDAGOGIQUES (Cours, TD, TP)
CREATE TABLE ressources_pedagogiques (
                                         id              BIGSERIAL PRIMARY KEY,
                                         titre           VARCHAR(200) NOT NULL,
                                         contenu         TEXT,
                                         type            VARCHAR(20) NOT NULL,
                                         fichier_url     VARCHAR(500),
                                         date_creation   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         chapitre_id     BIGINT NOT NULL,
                                         CONSTRAINT fk_ressource_chapitre FOREIGN KEY (chapitre_id) REFERENCES chapitres(id)
);

-- 6. QUESTIONS
CREATE TABLE questions (
                           id              BIGSERIAL PRIMARY KEY,
                           enonce          TEXT NOT NULL,
                           type            VARCHAR(20) NOT NULL,
                           difficulte      VARCHAR(20) NOT NULL,
                           points_defaut   DOUBLE PRECISION NOT NULL DEFAULT 1.0,
                           date_creation   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           chapitre_id     BIGINT NOT NULL,
                           CONSTRAINT fk_question_chapitre FOREIGN KEY (chapitre_id) REFERENCES chapitres(id)
);

-- 7. REPONSES POSSIBLES
CREATE TABLE reponses_possibles (
                                    id              BIGSERIAL PRIMARY KEY,
                                    texte           TEXT NOT NULL,
                                    est_correcte    BOOLEAN NOT NULL DEFAULT FALSE,
                                    ordre           INTEGER,
                                    question_id     BIGINT NOT NULL,
                                    CONSTRAINT fk_reponse_question FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- 8. EVALUATIONS
CREATE TABLE evaluations (
                             id                      BIGSERIAL PRIMARY KEY,
                             titre                   VARCHAR(200) NOT NULL,
                             type                    VARCHAR(20) NOT NULL,
                             date_debut              TIMESTAMP,
                             date_fin                TIMESTAMP,
                             duree_minutes           INTEGER,
                             nombre_tentatives_max   INTEGER NOT NULL DEFAULT 1,
                             ordre_aleatoire         BOOLEAN NOT NULL DEFAULT TRUE,
                             publie                  BOOLEAN NOT NULL DEFAULT FALSE,
                             chapitre_id             BIGINT,
                             matiere_id              BIGINT,
                             CONSTRAINT fk_evaluation_chapitre FOREIGN KEY (chapitre_id) REFERENCES chapitres(id),
                             CONSTRAINT fk_evaluation_matiere FOREIGN KEY (matiere_id) REFERENCES matieres(id)
);

-- 9. EVALUATION_QUESTIONS
CREATE TABLE evaluation_questions (
                                      id              BIGSERIAL PRIMARY KEY,
                                      points          DOUBLE PRECISION NOT NULL DEFAULT 1.0,
                                      ordre           INTEGER,
                                      evaluation_id   BIGINT NOT NULL,
                                      question_id     BIGINT NOT NULL,
                                      CONSTRAINT fk_eq_evaluation FOREIGN KEY (evaluation_id) REFERENCES evaluations(id),
                                      CONSTRAINT fk_eq_question FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- 10. TENTATIVES
CREATE TABLE tentatives (
                            id              BIGSERIAL PRIMARY KEY,
                            date_debut      TIMESTAMP NOT NULL,
                            date_fin        TIMESTAMP,
                            score           DOUBLE PRECISION,
                            statut          VARCHAR(20) NOT NULL DEFAULT 'EN_COURS',
                            etudiant_id     BIGINT NOT NULL,
                            evaluation_id   BIGINT NOT NULL,
                            CONSTRAINT fk_tentative_etudiant FOREIGN KEY (etudiant_id) REFERENCES users(id),
                            CONSTRAINT fk_tentative_evaluation FOREIGN KEY (evaluation_id) REFERENCES evaluations(id)
);

-- 11. REPONSES ETUDIANT
CREATE TABLE reponses_etudiant (
                                   id                      BIGSERIAL PRIMARY KEY,
                                   score_question          DOUBLE PRECISION,
                                   tentative_id            BIGINT NOT NULL,
                                   evaluation_question_id  BIGINT NOT NULL,
                                   CONSTRAINT fk_re_tentative FOREIGN KEY (tentative_id) REFERENCES tentatives(id),
                                   CONSTRAINT fk_re_eq FOREIGN KEY (evaluation_question_id) REFERENCES evaluation_questions(id)
);

-- 12. REPONSES ETUDIANT CHOIX
CREATE TABLE reponses_etudiant_choix (
                                         id                      BIGSERIAL PRIMARY KEY,
                                         reponse_etudiant_id     BIGINT NOT NULL,
                                         reponse_possible_id     BIGINT NOT NULL,
                                         CONSTRAINT fk_rec_reponse_etudiant FOREIGN KEY (reponse_etudiant_id) REFERENCES reponses_etudiant(id),
                                         CONSTRAINT fk_rec_reponse_possible FOREIGN KEY (reponse_possible_id) REFERENCES reponses_possibles(id)
);

-- 13. ALERTES
CREATE TABLE alertes (
                         id              BIGSERIAL PRIMARY KEY,
                         type            VARCHAR(50) NOT NULL,
                         date_heure      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         gravite         INTEGER DEFAULT 1,
                         description     TEXT,
                         tentative_id    BIGINT NOT NULL,
                         question_id     BIGINT,
                         CONSTRAINT fk_alerte_tentative FOREIGN KEY (tentative_id) REFERENCES tentatives(id),
                         CONSTRAINT fk_alerte_question FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- 14. ENSEIGNANT_MATIERE
CREATE TABLE enseignant_matiere (
                                    id              BIGSERIAL PRIMARY KEY,
                                    enseignant_id   BIGINT NOT NULL,
                                    matiere_id      BIGINT NOT NULL,
                                    CONSTRAINT fk_em_enseignant FOREIGN KEY (enseignant_id) REFERENCES users(id),
                                    CONSTRAINT fk_em_matiere FOREIGN KEY (matiere_id) REFERENCES matieres(id),
                                    CONSTRAINT uq_enseignant_matiere UNIQUE (enseignant_id, matiere_id)
);

-- 15. ETUDIANT_MATIERE
CREATE TABLE etudiant_matiere (
                                  id              BIGSERIAL PRIMARY KEY,
                                  etudiant_id     BIGINT NOT NULL,
                                  matiere_id      BIGINT NOT NULL,
                                  CONSTRAINT fk_etm_etudiant FOREIGN KEY (etudiant_id) REFERENCES users(id),
                                  CONSTRAINT fk_etm_matiere FOREIGN KEY (matiere_id) REFERENCES matieres(id),
                                  CONSTRAINT uq_etudiant_matiere UNIQUE (etudiant_id, matiere_id)
);

-- 16. AUDIT_LOGS
CREATE TABLE audit_logs (
                            id              BIGSERIAL PRIMARY KEY,
                            action          VARCHAR(100) NOT NULL,
                            entite          VARCHAR(100),
                            entite_id       BIGINT,
                            date_heure      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            adresse_ip      VARCHAR(50),
                            description     TEXT,
                            utilisateur_id  BIGINT,
                            CONSTRAINT fk_audit_user FOREIGN KEY (utilisateur_id) REFERENCES users(id)
);