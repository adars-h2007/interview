import { getMasteryColor, CATEGORY_MAP, DIFFICULTY_MAP } from '../constants';

function QuestionList({ questions, loading, onReview, onDelete, onUpdate, onAddClick }) {
  if (loading) {
    return <div className="loading">Loading questions...</div>;
  }

  if (questions.length === 0) {
    return (
      <div className="empty-state">
        <div className="empty-state-icon">📭</div>
        <h3>No questions yet</h3>
        <p>Create your first interview prep question to get started!</p>
        <button className="btn-primary" style={{ marginTop: '1rem' }} onClick={onAddClick}>
          + Add Question
        </button>
      </div>
    );
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h2>All Questions ({questions.length})</h2>
        <button className="btn-primary" onClick={onAddClick}>+ Add Question</button>
      </div>

      <div className="questions-container">
        {questions.map(q => (
          <div key={q.id} className="question-card">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', gap: '1rem' }}>
              <h4 style={{ margin: 0, flex: 1 }}>{q.question}</h4>
              <span style={{ fontSize: '1.5rem' }}>{CATEGORY_MAP[q.category]?.icon}</span>
            </div>

            <div className="question-answer">{q.answer}</div>

            <div className="question-meta">
              <span className={`badge ${q.difficulty?.toLowerCase() || 'medium'}`}>
                {DIFFICULTY_MAP[q.difficulty]?.label}
              </span>
              <span>🔁 {q.repetitions} reps</span>
              <span>⚡ {q.easeFactor?.toFixed(2)}</span>
              <span style={{ marginLeft: 'auto' }}>
                🎯 {Math.round(Math.min(q.averageQuality * 20, 100))}%
              </span>
            </div>

            <div className="mastery-bar">
              <div
                className="mastery-fill"
                style={{
                  width: Math.min(q.averageQuality * 20, 100) + '%',
                  background: getMasteryColor(Math.min(q.averageQuality * 20, 100)),
                }}
              />
            </div>

            <div className="card-actions">
              <button className="btn-primary btn-small" onClick={() => onReview(q)}>
                ✅ Review
              </button>
              <button
                className="btn-secondary btn-small"
                onClick={() => {
                  const newDiff = {
                    EASY: 'MEDIUM',
                    MEDIUM: 'HARD',
                    HARD: 'EASY'
                  }[q.difficulty];
                  onUpdate(q.id, { difficulty: newDiff });
                }}
              >
                🎯 Adjust
              </button>
              <button
                className="btn-secondary btn-small"
                style={{ background: '#fee2e2', color: '#dc2626' }}
                onClick={() => onDelete(q.id)}
              >
                🗑️ Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default QuestionList;
