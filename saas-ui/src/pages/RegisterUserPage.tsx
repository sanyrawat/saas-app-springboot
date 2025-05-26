import React, { useState } from 'react';
import axios from 'axios';

const RegisterUserPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [tenantId, setTenantId] = useState<number>(0);
  const [message, setMessage] = useState('');

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    const tenantId = localStorage.getItem('tenantId');
    const token = localStorage.getItem('token');

    try {
      await axios.post('http://localhost:8080/api/auth/register', {
        email,
        password,
        tenantId
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setMessage('✅ User registered successfully!');
      setEmail('');
      setPassword('');
    } catch (err: any) {
      setMessage('❌ Registration failed: ' + (err.response?.data || err.message));
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Register New User</h2>
      <form onSubmit={handleRegister}>
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
        <button type="submit" style={{ marginTop: '10px' }}>Register</button>
      </form>
      {message && <p style={{ color: 'darkgreen' }}>{message}</p>}
    </div>
  );
};

export default RegisterUserPage;
