import { useEffect, useState, useRef, useCallback } from 'react';
import { useAuth } from './AuthContext';
import * as api from './api';
import { CATEGORIES } from './constants';
import Header from './components/Header';
import Dashboard from './components/Dashboard';
import QuestionList from './components/QuestionList';
import AddQuestionModal from './components/AddQuestionModal';
import ReviewModal from './components/ReviewModal';
import Toast from './components/Toast';
import Login from './pages/Login';
import Signup from './pages/Signup';
import './index.css';

function App() {
  const { user, token, loading: authLoading, logout } = useAuth();
  const [view, setView] = useState('dashboard');
  const [questions, setQuestions] = useState([]);
  const [dueQuestions, setDueQuestions] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [reviewingQuestion, setReviewingQuestion] = useState(null);
  const [refreshKey, setRefreshKey] = useState(0);
  const [authPage, setAuthPage] = useState('login');

  const toastRef = useRef(null);

  const notify = useCallback((msg, type = 'success') => {
    if (toastRef.current) {
      toastRef.current(msg, type);
    }
  }, []);

  useEffect(() => {
    if (!authLoading && token) {
      const loadData = async () => {
        try {
          setLoading(true);
          const [allQuestions, due, stats] = await Promise.all([
            api.listQuestions(selectedCategory, token),
            api.getDueQuestions(selectedCategory, token),
            api.getStats(token),
          ]);
          setQuestions(allQuestions);
          setDueQuestions(due);
          setStats(stats);
        } catch (err) {
          notify(err.message, 'error');
        } finally {
          setLoading(false);
        }
      };
      loadData();
    }
  }, [selectedCategory, refreshKey, token, authLoading, notify]);

  const handleCreateQuestion = async (data) => {
    try {
      await api.createQuestion(data, token);
      setShowAddModal(false);
      setRefreshKey(k => k + 1);
      notify('Question added!');
    } catch (err) {
      notify(err.message, 'error');
    }
  };

  const handleReviewQuestion = async (questionId, quality) => {
    try {
      await api.reviewQuestion(questionId, quality, token);
      setReviewingQuestion(null);
      setRefreshKey(k => k + 1);
      notify('Question reviewed! Interval updated.');
    } catch (err) {
      notify(err.message, 'error');
    }
  };

  const handleDeleteQuestion = async (id) => {
    if (!window.confirm('Delete this question?')) return;
    try {
      await api.deleteQuestion(id, token);
      setRefreshKey(k => k + 1);
      notify('Question deleted');
    } catch (err) {
      notify(err.message, 'error');
    }
  };

  const handleUpdateQuestion = async (id, data) => {
    try {
      await api.updateQuestion(id, data, token);
      setRefreshKey(k => k + 1);
      notify('Question updated');
    } catch (err) {
      notify(err.message, 'error');
    }
  };

  const filteredQuestions = searchQuery
    ? questions.filter(q =>
        q.question.toLowerCase().includes(searchQuery.toLowerCase()) ||
        q.answer.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : questions;

  if (authLoading) {
    return <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
      <div className="loading">Loading...</div>
    </div>;
  }

  if (!token || !user) {
    return (
      <div className="app">
        {authPage === 'login' ? (
          <Login onSwitchToSignup={() => setAuthPage('signup')} />
        ) : (
          <Signup onSwitchToLogin={() => setAuthPage('login')} />
        )}
      </div>
    );
  }

  return (
    <div className="app">
      <Header
        view={view}
        onViewChange={setView}
        selectedCategory={selectedCategory}
        onCategoryChange={setSelectedCategory}
        searchQuery={searchQuery}
        onSearchChange={setSearchQuery}
        user={user}
        onLogout={() => {
          logout();
          setAuthPage('login');
        }}
      />

      <main className="main-content">
        {view === 'dashboard' && (
          <Dashboard
            stats={stats}
            dueQuestions={dueQuestions}
            onReview={(q) => setReviewingQuestion(q)}
            onAddClick={() => setShowAddModal(true)}
            loading={loading}
          />
        )}

        {view === 'questions' && (
          <QuestionList
            questions={filteredQuestions}
            loading={loading}
            onReview={(q) => setReviewingQuestion(q)}
            onDelete={handleDeleteQuestion}
            onUpdate={handleUpdateQuestion}
            onAddClick={() => setShowAddModal(true)}
          />
        )}
      </main>

      {showAddModal && (
        <AddQuestionModal
          onClose={() => setShowAddModal(false)}
          onSubmit={handleCreateQuestion}
        />
      )}

      {reviewingQuestion && (
        <ReviewModal
          question={reviewingQuestion}
          onClose={() => setReviewingQuestion(null)}
          onSubmit={handleReviewQuestion}
        />
      )}

      <Toast ref={toastRef} />
    </div>
  );
}

export default App;
