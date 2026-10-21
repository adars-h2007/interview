import { useState, useRef, useImperativeHandle, forwardRef } from 'react';

const Toast = forwardRef((_, ref) => {
  const [toast, setToast] = useState(null);
  const timerRef = useRef(null);

  useImperativeHandle(ref, () => (msg, type = 'success') => {
    if (timerRef.current) clearTimeout(timerRef.current);
    setToast({ msg, type });
    timerRef.current = setTimeout(() => {
      setToast(null);
    }, 3000);
  });

  if (!toast) return null;

  return (
    <div className={`toast ${toast.type}`}>
      {toast.type === 'success' && '✅'}
      {toast.type === 'error' && '❌'}
      {' '}{toast.msg}
    </div>
  );
});

Toast.displayName = 'Toast';
export default Toast;
