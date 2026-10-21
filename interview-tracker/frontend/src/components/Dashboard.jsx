import { formatDate, getMasteryColor, CATEGORY_MAP, DIFFICULTY_MAP } from '../constants';

function Dashboard({ stats, dueQuestions, onReview, onAddClick, loading }) {
  if (loading) {
    return <div className="loading">Loading dashboard...</div>;
  }

  return (
    <div className="dashboard">
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">📚 Total Questions</div>
          <div className="stat-value">{stats?.totalQuestions || 0}</div>
          <div className="stat-subtext">in all categories</div>
        </div>

        <div className="stat-card">
          <div className="stat-label">🔴 Due for Review</div>
          <div className="stat-value">{stats?.dueQuestions || 0}</div>
          <div className="stat-subtext">ready to practice</div>
        </div>

        <div className="stat-card">
          <div className="stat-label">🎯 Mastered</div>
          <div className="stat-value">{stats?.masteredQuestions || 0}</div>
          <div className="stat-subtext">80%+ confidence</div>
        </div>

        <div className="stat-card">
          <div className="stat-label">📈 Average Mastery</div>
          <div className="stat-value">{Math.round(stats?.averageMastery * 10) / 10 || 0}%</div>
          <div className="stat-subtext">overall progress</div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1.5rem', marginTop: '2rem' }}>
        {stats?.categoryBreakdown && Object.entries(stats.categoryBreakdown).map(([catKey, count]) => {
          const cat = Object.values({ DSA: { key: 'DSA', label: 'DSA', icon: '🔢' }, JAVA: { key: 'JAVA', label: 'Java', icon: '☕' }, WEB_DEVELOPMENT: { key: 'WEB_DEVELOPMENT', label: 'Web Development', icon: '🌐' }, SYSTEM_DESIGN: { key: 'SYSTEM_DESIGN', label: 'System Design', icon: '🏗️' }, BEHAVIORAL: { key: 'BEHAVIORAL', label: 'Behavioral', icon: '💬' }, DATABASE: { key: 'DATABASE', label: 'Database', icon: '🗄️' }, NETWORKING: { key: 'NETWORKING', label: 'Networking', icon: '🌐' }, CLOUD: { key: 'CLOUD', label: 'Cloud & DevOps', icon: '☁️' }, OTHER: { key: 'OTHER', label: 'Other', icon: '✨' } }).find(c => c.key === catKey);
          return (
            <div key={catKey} className="stat-card">
              <div className="stat-label">{cat?.icon || '📌'} {cat?.label || 'Unknown'}</div>
              <div className="stat-value">{count}</div>
              <div className="stat-subtext">questions</div>
            </div>
          );
        })}
      </div>

      <div className="due-section">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h3 style={{ margin: 0 }}>🔴 Due for Review</h3>
          <button className="btn-primary" onClick={onAddClick}>+ Add Question</button>
        </div>

        {dueQuestions.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">✨</div>
            <h3>All caught up!</h3>
            <p>No questions due for review. Great work!</p>
            <button className="btn-primary" style={{ marginTop: '1rem' }} onClick={onAddClick}>Add a new question</button>
          </div>
        ) : (
          <div className="due-questions-list">
            {dueQuestions.slice(0, 10).map(q => (
              <div
                key={q.id}
                className="question-item"
                onClick={() => onReview(q)}
              >
                <div className="question-header">
                  <span style={{ flex: 1 }}>{q.question}</span>
                  <span style={{ whiteSpace: 'nowrap' }}>{CATEGORY_MAP[q.category]?.icon}</span>
                </div>
                <div className="question-meta">
                  <span className={`badge ${q.difficulty?.toLowerCase() || 'medium'}`}>
                    {DIFFICULTY_MAP[q.difficulty]?.label || q.difficulty}
                  </span>
                  <span>📅 {formatDate(q.nextReviewAt)}</span>
                  <span>🔁 Rep: {q.repetitions}</span>
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
              </div>
            ))}
            {dueQuestions.length > 10 && (
              <p style={{ textAlign: 'center', color: '#475569', marginTop: '1rem' }}>
                +{dueQuestions.length - 10} more questions due
              </p>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
