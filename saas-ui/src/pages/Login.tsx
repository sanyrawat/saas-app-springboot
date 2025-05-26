// src/pages/Login.tsx
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [tenantId, setTenantId] = useState<number>(0);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();


  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        email,
        password,
        tenantId
      });
  
      localStorage.setItem('token', response.data);         // Store token
      localStorage.setItem('tenantId', tenantId.toString()); // ✅ Store tenantId
  
      setMessage('✅ Login successful!');
      navigate('/dashboard');
  
    } catch (err: any) {
      setMessage('❌ Login failed: ' + (err.response?.data || err.message));
    }
  };
  

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label>Email:</label><br />
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} required />
        </div>
        <div style={{ marginTop: '10px' }}>
          <label>Password:</label><br />
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </div>
        <div style={{ marginTop: '10px' }}>
          <label>Tenant ID:</label><br />
          <input
            type="text"
            value={tenantId}
            onChange={e => setTenantId(Number(e.target.value))}
            required
          />
        </div>
        <button type="submit" style={{ marginTop: '10px' }}>Login</button>
      </form>
      <div style={{ marginTop: '20px' }}>
        <button onClick={() => navigate('/register-user')}>
          ➕ Register New User
        </button>
      </div>
      {message && <p style={{ color: 'darkred' }}>{message}</p>}
    </div>
  );
};

export default Login;
