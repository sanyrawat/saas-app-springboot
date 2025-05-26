// src/App.tsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import RegisterUserPage from './pages/RegisterUserPage';
import AssignRole from './pages/AssignRole';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="*" element={<Login />} />
        <Route path="/register-user" element={<RegisterUserPage />} />
        <Route path="/assign-role" element={<AssignRole />} />
      </Routes>
    </Router>
  );
};

export default App;
