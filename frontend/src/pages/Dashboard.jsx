import { useAuth } from '../context/AuthContext';

function Dashboard() {
  const { user, logout } = useAuth();

  return (
    <div>
      <h2>Tableau de bord</h2>
      <p>Bienvenue, {user.prenom} {user.nom} ({user.role})</p>
      <button onClick={logout}>Se déconnecter</button>
    </div>
  );
}

export default Dashboard;