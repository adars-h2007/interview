import { useState } from 'react';
import { CATEGORIES, DIFFICULTIES } from '../constants';

function AddQuestionModal({ onClose, onSubmit }) {
  const [formData, setFormData] = useState({
    question: '',
    answer: '',
    category: 'DSA',
    difficulty: 'MEDIUM',
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.question.trim() || !formData.answer.trim()) {
      alert('Please fill in all fields');
      return;
    }
    onSubmit(formData);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>📝 Add Question</h2>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Question *</label>
            <textarea
              name="question"
              value={formData.question}
              onChange={handleChange}
              placeholder="Enter the interview question..."
              required
            />
          </div>

          <div className="form-group">
            <label>Answer *</label>
            <textarea
              name="answer"
              value={formData.answer}
              onChange={handleChange}
              placeholder="Enter the answer or solution..."
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div className="form-group">
              <label>Category</label>
              <select
                name="category"
                value={formData.category}
                onChange={handleChange}
              >
                {CATEGORIES.map(cat => (
                  <option key={cat.key} value={cat.key}>
                    {cat.icon} {cat.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Initial Difficulty</label>
              <select
                name="difficulty"
                value={formData.difficulty}
                onChange={handleChange}
              >
                {DIFFICULTIES.map(diff => (
                  <option key={diff.key} value={diff.key}>
                    {diff.label}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="btn-secondary" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn-primary">
              ✅ Add Question
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddQuestionModal;
