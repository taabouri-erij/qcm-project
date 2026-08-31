import { Navbar, Container, Nav, Button } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';

function Layout({ children }) {
  const { user, logout } = useAuth();

  return (
    <>
      <Navbar bg="dark" variant="dark" className="mb-4">
        <Container>
          <Navbar.Brand>QCM Platform</Navbar.Brand>
          <Nav className="ms-auto d-flex align-items-center gap-3">
            <span className="text-light">
              {user.prenom} {user.nom} — {user.role}
            </span>
            <Button variant="outline-light" size="sm" onClick={logout}>
              Se déconnecter
            </Button>
          </Nav>
        </Container>
      </Navbar>
      <Container>{children}</Container>
    </>
  );
}

export default Layout;