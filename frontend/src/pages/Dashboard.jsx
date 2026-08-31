import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const navigate = useNavigate();
  const nom = localStorage.getItem('nom');
  const prenom = localStorage.getItem('prenom');
  const role = localStorage.getItem('role');

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  return (
    <div>
      <h2>Tableau de bord</h2>
      <p>Bienvenue, {prenom} {nom} ({role})</p>
      <button onClick={handleLogout}>Se déconnecter</button>
    </div>
  );
}

export default Dashboard;