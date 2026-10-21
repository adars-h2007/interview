import { useState } from 'react';
import { QUALITIES, CATEGORY_MAP, DIFFICULTY_MAP } from '../constants';

function ReviewModal({ question, onClose, onSubmit }) {
  const [quality, setQuality] = useState(null);

  const handleSubmit = () => {
    if (quality === null) {
      alert('Please rate your performance');
      return;
    }
    onSubmit(question.id, quality);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>🎯 Review Question</h2>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        <div style={{ marginBottom: '1.5rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', gap: '1rem', marginBottom: '1rem' }}>
            <h4 style={{ margin: 0, flex: 1 }}>{question.question}</h4>
            <span style={{ fontSize: '1.25rem' }}>{CATEGORY_MAP[question.category]?.icon}</span>
          </div>
          <span className={`badge ${question.difficulty?.toLowerCase() || 'medium'}`}>
            {DIFFICULTY_MAP[question.difficulty]?.label}
          </span>
        </div>

        <div className="question-answer">{question.answer}</div>

        <div style={{ marginTop: '2rem' }}>
          <h4 style={{ marginBottom: '1rem', textAlign: 'center' }}>How well did you answer?</h4>
          <div className="quality-buttons">
            {QUALITIES.map(q => (
              <button
                key={q.value}
                className={`quality-btn ${quality === q.value ? 'selected' : ''}`}
                onClick={() => setQuality(q.value)}
              >
                <span className="quality-emoji">{q.emoji}</span>
                <span className="quality-label">{q.label}</span>
              </button>
            ))}
          </div>
        </div>

        <div style={{ marginTop: '2rem', padding: '1rem', background: '#f0f9ff', borderRadius: '8px', fontSize: '0.9rem' }}>
          <p style={{ margin: 0 }}>
            📌 <strong>Spaced Repetition:</strong> Your next review will be scheduled based on your performance and the SM-2 algorithm.
          </p>
        </div>

        <div className="form-actions" style={{ marginTop: '2rem' }}>
          <button className="btn-secondary" onClick={onClose}>
            Cancel
          </button>
          <button className="btn-primary" onClick={handleSubmit}>
            ✅ Submit Review
          </button>
        </div>
      </div>
    </div>
  );
}

export default ReviewModal;
