export const CATEGORIES = [
  { key: 'DSA', label: 'Data Structures & Algorithms', icon: '🔢' },
  { key: 'JAVA', label: 'Java', icon: '☕' },
  { key: 'WEB_DEVELOPMENT', label: 'Web Development', icon: '🌐' },
  { key: 'SYSTEM_DESIGN', label: 'System Design', icon: '🏗️' },
  { key: 'BEHAVIORAL', label: 'Behavioral', icon: '💬' },
  { key: 'DATABASE', label: 'Database', icon: '🗄️' },
  { key: 'NETWORKING', label: 'Networking', icon: '🌐' },
  { key: 'CLOUD', label: 'Cloud & DevOps', icon: '☁️' },
  { key: 'OTHER', label: 'Other', icon: '✨' },
];

export const CATEGORY_MAP = Object.fromEntries(
  CATEGORIES.map(c => [c.key, c])
);

export const DIFFICULTIES = [
  { key: 'EASY', label: 'Easy', color: '#10b981' },
  { key: 'MEDIUM', label: 'Medium', color: '#f59e0b' },
  { key: 'HARD', label: 'Hard', color: '#ef4444' },
];

export const DIFFICULTY_MAP = Object.fromEntries(
  DIFFICULTIES.map(d => [d.key, d])
);

export const QUALITIES = [
  { value: 5, label: 'Perfect', emoji: '😍' },
  { value: 4, label: 'Good', emoji: '😊' },
  { value: 3, label: 'Fair', emoji: '😐' },
  { value: 2, label: 'Difficult', emoji: '😕' },
  { value: 1, label: 'Wrong', emoji: '😞' },
];

export const timeAgo = (isoDate) => {
  if (!isoDate) return 'Never';
  const date = new Date(String(isoDate).slice(0, 19));
  const seconds = Math.floor((new Date() - date) / 1000);
  if (seconds < 60) return 'Just now';
  if (seconds < 3600) return `${Math.floor(seconds / 60)}m ago`;
  if (seconds < 86400) return `${Math.floor(seconds / 3600)}h ago`;
  return `${Math.floor(seconds / 86400)}d ago`;
};

export const formatDate = (isoDate) => {
  if (!isoDate) return 'Never';
  const date = new Date(String(isoDate).slice(0, 19));
  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

export const getMasteryColor = (score) => {
  if (score >= 80) return '#10b981';
  if (score >= 60) return '#3b82f6';
  if (score >= 40) return '#f59e0b';
  return '#ef4444';
};

export const getReadinessEmoji = (status) => {
  switch (status) {
    case 'READY':
    case 'OVERDUE':
      return '🔴';
    case 'DUE_SOON':
      return '🟡';
    default:
      return '🟢';
  }
};
