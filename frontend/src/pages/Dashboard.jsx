import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

function Dashboard() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [chargement, setChargement] = useState(true);
  const [tentatives, setTentatives] = useState([]);
  const [matieres, setMatieres] = useState([]);
  const [evaluations, setEvaluations] = useState([]);

  useEffect(() => {
    async function chargerDonnees() {
      try {
        // 1. Tentatives de l'étudiant (résultats + tentative en cours)
        const resTentatives = await api.get(`/tentatives/etudiant/${user.userId}`);
        setTentatives(resTentatives.data);

        // 2. Matières inscrites
        const resMatieres = await api.get(`/etudiant-matieres/etudiant/${user.userId}`);
        setMatieres(resMatieres.data);

        // 3. Évaluations de chaque matière inscrite
        const listesEvaluations = await Promise.all(
          resMatieres.data.map((m) => api.get(`/evaluations/matiere/${m.matiereId}`))
        );
        const toutesEvaluations = listesEvaluations.flatMap((r) => r.data);
        setEvaluations(toutesEvaluations);

      } catch (err) {
        console.error('Erreur chargement dashboard', err);
      } finally {
        setChargement(false);
      }
    }

    chargerDonnees();
  }, [user.userId]);

  const handleCommencer = async (evaluationId) => {
    try {
      const res = await api.post('/tentatives/demarrer', {
        etudiantId: Number(user.userId),
        evaluationId: evaluationId,
      });
      navigate(`/passage/${res.data.id}`);
    } catch (err) {
      alert(err.response?.data?.message || "Impossible de démarrer l'évaluation");
    }
  };

  if (chargement) {
    return <p>Chargement du tableau de bord...</p>;
  }

  const maintenant = new Date();

  // Tentative en cours (une seule normalement)
  const tentativeEnCours = tentatives.find((t) => t.statut === 'EN_COURS');

  // Résultats = tentatives soumises
  const resultats = tentatives.filter((t) => t.statut === 'SOUMIS');

  // Nombre de tentatives déjà soumises par évaluation (pour calculer les tentatives restantes)
  const nbTentativesSoumisesParEvaluation = {};
  tentatives.forEach((t) => {
    if (t.statut === 'SOUMIS') {
      nbTentativesSoumisesParEvaluation[t.evaluationId] =
        (nbTentativesSoumisesParEvaluation[t.evaluationId] || 0) + 1;
    }
  });

  // Évaluations disponibles : publiées, dans la fenêtre de dates, tentatives restantes > 0,
  // et pas déjà une tentative EN_COURS dessus
  const evaluationsDisponibles = evaluations.filter((e) => {
    if (!e.publie) return false;
    const debut = new Date(e.dateDebut);
    const fin = new Date(e.dateFin);
    if (maintenant < debut || maintenant > fin) return false;

    const dejaFaites = nbTentativesSoumisesParEvaluation[e.id] || 0;
    if (dejaFaites >= e.nombreTentativesMax) return false;

    if (tentativeEnCours && tentativeEnCours.evaluationId === e.id) return false;

    return true;
  });

  // Prochaines évaluations : publiées, mais qui n'ont pas encore commencé
  const prochainesEvaluations = evaluations.filter((e) => {
    if (!e.publie) return false;
    const debut = new Date(e.dateDebut);
    return maintenant < debut;
  });

  return (
    <div>
      <h2>Tableau de bord</h2>
      <p>Bienvenue, {user.prenom} {user.nom} ({user.role})</p>

      {/* 🔴 Évaluations disponibles */}
      <h4 className="mt-4">🔴 Évaluations disponibles</h4>
      {evaluationsDisponibles.length === 0 && <p>Aucune évaluation disponible pour le moment.</p>}
      {evaluationsDisponibles.map((e) => {
        const dejaFaites = nbTentativesSoumisesParEvaluation[e.id] || 0;
        const restantes = e.nombreTentativesMax - dejaFaites;
        return (
          <div key={e.id} className="border rounded p-2 mb-2">
            <strong>{e.titre}</strong> ({e.type}) — {e.matiereNom}
            <br />
            Date limite : {new Date(e.dateFin).toLocaleDateString()} — Durée : {e.dureeMinutes} min
            — Tentatives restantes : {restantes}
            <br />
            <button onClick={() => handleCommencer(e.id)}>Commencer</button>
          </div>
        );
      })}

      {/* 🟠 Évaluation en cours */}
      <h4 className="mt-4">🟠 Évaluation en cours</h4>
      {!tentativeEnCours && <p>Aucune évaluation en cours.</p>}
      {tentativeEnCours && (
        <div className="border rounded p-2 mb-2">
          <strong>{tentativeEnCours.evaluationTitre}</strong>
          <br />
          Démarrée le : {new Date(tentativeEnCours.dateDebut).toLocaleString()}
          <br />
          <button onClick={() => navigate(`/passage/${tentativeEnCours.id}`)}>Reprendre</button>
        </div>
      )}

      {/* 🔵 Prochaines évaluations */}
      <h4 className="mt-4">🔵 Prochaines évaluations</h4>
      {prochainesEvaluations.length === 0 && <p>Aucune évaluation à venir.</p>}
      {prochainesEvaluations.map((e) => (
        <div key={e.id} className="border rounded p-2 mb-2">
          <strong>{e.titre}</strong> — {e.matiereNom} — Prévue le : {new Date(e.dateDebut).toLocaleDateString()}
        </div>
      ))}

      {/* 🟢 Mes résultats */}
      <h4 className="mt-4">🟢 Mes résultats</h4>
      {resultats.length === 0 && <p>Aucun résultat pour l'instant.</p>}
      {resultats.map((t) => (
        <div key={t.id} className="border rounded p-2 mb-2">
          <strong>{t.evaluationTitre}</strong> — Note : {t.score}/20
        </div>
      ))}

      {/* Mes matières */}
      <h4 className="mt-4">Mes matières</h4>
      {matieres.length === 0 && <p>Aucune matière inscrite.</p>}
      <ul>
        {matieres.map((m) => (
          <li key={m.id}>{m.matiereNom}</li>
        ))}
      </ul>
    </div>
  );
}

export default Dashboard;