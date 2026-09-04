import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Passage from './pages/Passage';
import RouteProtegee from './components/RouteProtegee';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route
            path="/dashboard"
            element={
              <RouteProtegee>
                <Dashboard />
              </RouteProtegee>
            }
          />
          <Route
            path="/passage/:tentativeId"
            element={
              <RouteProtegee>
                <Passage />
              </RouteProtegee>
            }
          />
          <Route path="/" element={<Navigate to="/login" />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;