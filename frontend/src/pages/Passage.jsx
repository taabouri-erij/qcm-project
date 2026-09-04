import { useState, useEffect, useCallback, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Badge, Form, Button, Spinner, Alert } from 'react-bootstrap';
import api from '../services/api';

function Passage() {
  const { tentativeId } = useParams();
  const navigate = useNavigate();

  const [chargement, setChargement] = useState(true);
  const [erreur, setErreur] = useState('');
  const [erreurBloquante, setErreurBloquante] = useState(false);
  const [evaluation, setEvaluation] = useState(null);
  const [questions, setQuestions] = useState([]); // EvaluationQuestionDTO triées par ordre
  const [reponsesChoisies, setReponsesChoisies] = useState({}); // { evaluationQuestionId: [reponsePossibleId, ...] }
  const [indexActuel, setIndexActuel] = useState(0);
  const [secondesRestantes, setSecondesRestantes] = useState(null);
  const [enEnregistrement, setEnEnregistrement] = useState(false);
  const soumissionEnCours = useRef(false);

  // Chargement initial : tentative -> évaluation (avec questions) -> réponses déjà données
  useEffect(() => {
    async function charger() {
      try {
        const resTentative = await api.get(`/tentatives/${tentativeId}`);
        const tentative = resTentative.data;

        if (tentative.statut !== 'EN_COURS') {
          setErreur("Cette tentative n'est plus active (déjà soumise, expirée ou annulée).");
          setErreurBloquante(true);
          setChargement(false);
          return;
        }

        const resEvaluation = await api.get(`/evaluations/${tentative.evaluationId}`);
        const evaluationData = resEvaluation.data;
        const questionsTriees = [...evaluationData.questions].sort((a, b) => a.ordre - b.ordre);

        const resReponses = await api.get(`/reponses-etudiant/tentative/${tentativeId}`);
        const carteReponses = {};
        resReponses.data.forEach((r) => {
          carteReponses[r.evaluationQuestionId] = r.reponsesChoisiesIds || [];
        });

        // Temps restant = (début de la tentative + durée de l'évaluation) - maintenant
        const finPrevue =
          new Date(tentative.dateDebut).getTime() + evaluationData.dureeMinutes * 60 * 1000;
        const restant = Math.max(0, Math.floor((finPrevue - Date.now()) / 1000));

        setEvaluation(evaluationData);
        setQuestions(questionsTriees);
        setReponsesChoisies(carteReponses);
        setSecondesRestantes(restant);
      } catch (err) {
        setErreur("Impossible de charger l'examen. " + (err.response?.data?.message || ''));
        setErreurBloquante(true);
      } finally {
        setChargement(false);
      }
    }
    charger();
  }, [tentativeId]);

  // Soumission (déclenchée manuellement par l'étudiant, ou automatiquement à la fin du temps)
  const handleSoumettre = useCallback(async () => {
    if (soumissionEnCours.current) return;
    soumissionEnCours.current = true;
    try {
      const res = await api.post(`/tentatives/${tentativeId}/soumettre`);
      navigate('/dashboard', { state: { derniereNote: res.data.score } });
    } catch (err) {
      setErreur('Erreur lors de la soumission. ' + (err.response?.data?.message || ''));
      soumissionEnCours.current = false;
    }
  }, [tentativeId, navigate]);

  // Décompte du temps ; soumission automatique dès que le temps est écoulé
  useEffect(() => {
    if (secondesRestantes === null) return;
    if (secondesRestantes <= 0) {
      queueMicrotask(handleSoumettre);
      return;
    }
    const minuteur = setInterval(() => setSecondesRestantes((s) => s - 1), 1000);
    return () => clearInterval(minuteur);
  }, [secondesRestantes, handleSoumettre]);

  // Envoie la sélection courante au backend (upsert géré côté serveur)
  const envoyerReponse = async (evaluationQuestionId, idsChoisis) => {
    setReponsesChoisies((prev) => ({ ...prev, [evaluationQuestionId]: idsChoisis }));
    setEnEnregistrement(true);
    try {
      await api.post('/passage/repondre', {
        tentativeId: Number(tentativeId),
        evaluationQuestionId,
        reponsesPossiblesIds: idsChoisis,
      });
    } catch (err) {
      setErreur("Impossible d'enregistrer cette réponse. " + (err.response?.data?.message || ''));
    } finally {
      setEnEnregistrement(false);
    }
  };

  const handleChoixSimple = (evaluationQuestionId, reponseId) => {
    envoyerReponse(evaluationQuestionId, [reponseId]);
  };

  const handleChoixMultiple = (evaluationQuestionId, reponseId, coche) => {
    const actuel = reponsesChoisies[evaluationQuestionId] || [];
    const nouveau = coche ? [...actuel, reponseId] : actuel.filter((id) => id !== reponseId);
    envoyerReponse(evaluationQuestionId, nouveau);
  };

  if (chargement) {
    return (
      <div className="text-center mt-5">
        <Spinner animation="border" />
      </div>
    );
  }

  if (erreurBloquante) {
    return (
      <div>
        <Alert variant="danger">{erreur}</Alert>
        <Button onClick={() => navigate('/dashboard')}>Retour au tableau de bord</Button>
      </div>
    );
  }

  const eq = questions[indexActuel]; // EvaluationQuestionDTO courant
  const q = eq.question; // QuestionDTO
  const mesReponses = reponsesChoisies[eq.id] || [];
  const reponsesTriees = [...q.reponsesPossibles].sort((a, b) => a.ordre - b.ordre);

  const minutes = Math.floor(secondesRestantes / 60);
  const secondes = secondesRestantes % 60;
  const tempsUrgent = secondesRestantes < 60;

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h3>{evaluation.titre}</h3>
        <Badge bg={tempsUrgent ? 'danger' : 'dark'} style={{ fontSize: '1.1rem' }}>
          ⏱ {String(minutes).padStart(2, '0')}:{String(secondes).padStart(2, '0')}
        </Badge>
      </div>

      {erreur && (
        <Alert variant="warning" onClose={() => setErreur('')} dismissible>
          {erreur}
        </Alert>
      )}

      {/* Navigateur de questions : vert = répondue, bleu = affichée */}
      <div className="mb-3 d-flex flex-wrap gap-2">
        {questions.map((eqItem, i) => {
          const repondue = (reponsesChoisies[eqItem.id] || []).length > 0;
          return (
            <Button
              key={eqItem.id}
              size="sm"
              variant={i === indexActuel ? 'primary' : repondue ? 'success' : 'outline-secondary'}
              onClick={() => setIndexActuel(i)}
            >
              {i + 1}
            </Button>
          );
        })}
      </div>

      <Card className="mb-3">
        <Card.Body>
          <div className="d-flex justify-content-between">
            <Card.Subtitle className="mb-2 text-muted">
              Question {indexActuel + 1} / {questions.length} — {eq.points} pt
              {eq.points > 1 ? 's' : ''}
            </Card.Subtitle>
            <Badge bg="secondary">{q.difficulte}</Badge>
          </div>
          <Card.Text style={{ fontSize: '1.1rem' }}>{q.enonce}</Card.Text>

          <Form>
            {q.type === 'QCM_SIMPLE'
              ? reponsesTriees.map((rp) => (
                  <Form.Check
                    key={rp.id}
                    type="radio"
                    name={`question-${eq.id}`}
                    id={`reponse-${rp.id}`}
                    label={rp.texte}
                    checked={mesReponses.includes(rp.id)}
                    onChange={() => handleChoixSimple(eq.id, rp.id)}
                    className="mb-2"
                  />
                ))
              : reponsesTriees.map((rp) => (
                  <Form.Check
                    key={rp.id}
                    type="checkbox"
                    id={`reponse-${rp.id}`}
                    label={rp.texte}
                    checked={mesReponses.includes(rp.id)}
                    onChange={(e) => handleChoixMultiple(eq.id, rp.id, e.target.checked)}
                    className="mb-2"
                  />
                ))}
          </Form>
        </Card.Body>
      </Card>

      <div className="d-flex justify-content-between align-items-center">
        <Button
          variant="outline-secondary"
          disabled={indexActuel === 0}
          onClick={() => setIndexActuel((i) => i - 1)}
        >
          ← Précédent
        </Button>

        {enEnregistrement && <small className="text-muted">Enregistrement...</small>}

        {indexActuel < questions.length - 1 ? (
          <Button variant="outline-secondary" onClick={() => setIndexActuel((i) => i + 1)}>
            Suivant →
          </Button>
        ) : (
          <Button
            variant="danger"
            onClick={() => {
              if (window.confirm("Soumettre définitivement l'examen ? Cette action est irréversible.")) {
                handleSoumettre();
              }
            }}
          >
            Soumettre l'examen
          </Button>
        )}
      </div>
    </div>
  );
}

export default Passage;