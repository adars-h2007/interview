const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const request = async (endpoint, options = {}, token = null) => {
  const url = `${BASE}${endpoint}`;
  const headers = { 'Content-Type': 'application/json', ...options.headers };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const res = await fetch(url, {
    headers,
    ...options,
  });

  const body = await res.json();
  if (!res.ok) {
    throw new Error(body.error || `HTTP ${res.status}`);
  }
  return body;
};

// Auth endpoints
export const register = (name, email, password) =>
  request('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ name, email, password }),
  });

export const login = (email, password) =>
  request('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  });

// Question endpoints (require token)
export const listQuestions = (category = null, token) => {
  const url = category ? `/questions?category=${category}` : '/questions';
  return request(url, {}, token);
};

export const getDueQuestions = (category = null, token) => {
  const url = category ? `/questions/due?category=${category}` : '/questions/due';
  return request(url, {}, token);
};

export const getQuestion = (id, token) => request(`/questions/${id}`, {}, token);

export const createQuestion = (data, token) =>
  request('/questions', {
    method: 'POST',
    body: JSON.stringify(data),
  }, token);

export const updateQuestion = (id, data, token) =>
  request(`/questions/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  }, token);

export const reviewQuestion = (id, quality, token) =>
  request(`/questions/${id}/review`, {
    method: 'POST',
    body: JSON.stringify({ quality }),
  }, token);

export const deleteQuestion = (id, token) =>
  request(`/questions/${id}`, { method: 'DELETE' }, token);

export const searchQuestions = (q, token) =>
  request(`/questions/search?q=${encodeURIComponent(q)}`, {}, token);

export const getStats = (token) => request('/questions/stats/all', {}, token);
