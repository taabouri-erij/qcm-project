import { useAuth } from '../context/AuthContext';

function Dashboard() {
  const { user } = useAuth();

  return (
    <div>
      <h2>Tableau de bord</h2>
      <p>Bienvenue, {user.prenom} {user.nom} ({user.role})</p>
    </div>
  );
}

export default Dashboard;
