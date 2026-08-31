import { createContext, useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const navigate = useNavigate();

  const [user, setUser] = useState(() => {
    const token = localStorage.getItem('token');
    if (!token) return null;

    return {
      token,
      userId: localStorage.getItem('userId'),
      nom: localStorage.getItem('nom'),
      prenom: localStorage.getItem('prenom'),
      role: localStorage.getItem('role'),
    };
  });

  const login = (data) => {
    localStorage.setItem('token', data.token);
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('role', data.role);
    localStorage.setItem('nom', data.nom);
    localStorage.setItem('prenom', data.prenom);

    setUser({
      token: data.token,
      userId: data.userId,
      nom: data.nom,
      prenom: data.prenom,
      role: data.role,
    });
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
    navigate('/login');
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// Hook personnalisé, pour accéder facilement au contexte depuis n'importe quel composant
export function useAuth() {
  return useContext(AuthContext);
}