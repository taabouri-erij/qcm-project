import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

function Dashboard() {
  const { user } = useAuth();
  const [tentatives, setTentatives] = useState([]);
  const [chargement, setChargement] = useState(true);

  useEffect(() => {
    api.get(`/tentatives/etudiant/${user.userId}`)
      .then((response) => {
        setTentatives(response.data);
        setChargement(false);
      })
      .catch(() => setChargement(false));
  }, [user.userId]);

  return (
    <div>
      <h2>Tableau de bord</h2>
      <p>Bienvenue, {user.prenom} {user.nom} ({user.role})</p>

      <h4 className="mt-4">🟢 Mes résultats</h4>
      {chargement && <p>Chargement...</p>}
      {!chargement && tentatives.length === 0 && <p>Aucune tentative pour l'instant.</p>}
      {!chargement && tentatives.map((t) => (
        <div key={t.id} className="border rounded p-2 mb-2">
          <strong>{t.evaluationTitre}</strong> — Statut : {t.statut}
          {t.score !== null && <span> — Note : {t.score}/20</span>}
        </div>
      ))}
    </div>
  );
}

export default Dashboard;