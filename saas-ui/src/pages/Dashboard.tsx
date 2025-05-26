// src/pages/Dashboard.tsx
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import UsersPage from './UsersPage';

const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    }
  }, [navigate]);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>🔐 Welcome to Dashboard</h2>
      <p>You are logged in. JWT is stored in localStorage ✅</p>
      <button onClick={() => {
        localStorage.removeItem('token');
        navigate('/login');
      }}>Logout</button>
      <UsersPage></UsersPage>
    </div>
  );
};

export default Dashboard;
