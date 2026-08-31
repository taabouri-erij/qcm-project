import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// Intercepteur : s'exécute automatiquement AVANT chaque requête
// -> on y attache le token si l'utilisateur est connecté
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Intercepteur : s'exécute automatiquement APRES chaque réponse
// -> si le token est invalide/expiré (401), on déconnecte automatiquement
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;