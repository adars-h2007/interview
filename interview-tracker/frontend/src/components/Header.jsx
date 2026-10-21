import { CATEGORIES } from '../constants';

function Header({ view, onViewChange, selectedCategory, onCategoryChange, searchQuery, onSearchChange, user, onLogout }) {
  return (
    <header className="header">
      <div className="header-inner">
        <div className="logo">
          <span>🧠</span>
          <span>Recall</span>
        </div>

        <nav className="nav-tabs">
          <button
            className={`nav-tab ${view === 'dashboard' ? 'active' : ''}`}
            onClick={() => onViewChange('dashboard')}
          >
            📊 Dashboard
          </button>
          <button
            className={`nav-tab ${view === 'questions' ? 'active' : ''}`}
            onClick={() => onViewChange('questions')}
          >
            📚 Questions
          </button>
        </nav>

        <div className="search-box">
          <input
            type="text"
            placeholder="Search questions..."
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
          />
        </div>

        {user && (
          <div className="user-menu">
            <span className="user-name">👤 {user.name}</span>
            <button className="btn-secondary btn-small" onClick={onLogout}>
              Sign Out
            </button>
          </div>
        )}
      </div>

      <div className="category-filter" style={{ marginTop: '0.75rem', paddingLeft: 'calc(50px + 1rem)' }}>
        <button
          className={`category-btn ${!selectedCategory ? 'active' : ''}`}
          onClick={() => onCategoryChange(null)}
        >
          All
        </button>
        {CATEGORIES.map(cat => (
          <button
            key={cat.key}
            className={`category-btn ${selectedCategory === cat.key ? 'active' : ''}`}
            onClick={() => onCategoryChange(cat.key)}
          >
            {cat.icon} {cat.label}
          </button>
        ))}
      </div>
    </header>
  );
}

export default Header;
