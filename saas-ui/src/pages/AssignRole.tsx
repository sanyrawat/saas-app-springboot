// src/pages/AssignRole.tsx
import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface User {
  id: number;
  email: string;
}

const AssignRole: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUserId, setSelectedUserId] = useState<number>(0);
  const [selectedRole, setSelectedRole] = useState('');
  const [message, setMessage] = useState('');
  const tenantId = localStorage.getItem('tenantId');
  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/tenants/${tenantId}/users`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setUsers(response.data);
      } catch (err: any) {
        setMessage('Failed to fetch users');
      }
    };

    fetchUsers();
  }, [tenantId, token]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
        await axios.post(
            'http://localhost:8080/api/users/assign-role',
            {
              userId: selectedUserId,
              role: selectedRole,  // Must be one of: "SYSTEM_ADMIN", "SUPPORT_ADMIN", etc.
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
      setMessage('✅ Role assigned successfully');
    } catch (err: any) {
      setMessage('❌ Failed to assign role: ' + (err.response?.data || err.message));
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Assign Role</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>User:</label><br />
          <select value={selectedUserId} onChange={e => setSelectedUserId(Number(e.target.value))} required>
            <option value="">-- Select User --</option>
            {users.map(user => (
              <option key={user.id} value={user.id}>{user.email}</option>
            ))}
          </select>
        </div>

        <div style={{ marginTop: '10px' }}>
          <label>Role:</label><br />
          <select value={selectedRole} onChange={e => setSelectedRole(e.target.value)} required>
            <option value="">-- Select Role --</option>
            <option value="SUPPORT_ADMIN">Support Admin</option>
            <option value="DEVOPS_ADMIN">DevOps Admin</option>
            <option value="BILLING_ADMIN">Billing Admin</option>
          </select>
        </div>

        <button type="submit" style={{ marginTop: '15px' }}>Assign Role</button>
      </form>

      {message && <p style={{ marginTop: '1rem', color: 'darkblue' }}>{message}</p>}
    </div>
  );
};

export default AssignRole;
