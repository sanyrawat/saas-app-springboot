import React, { useEffect, useState } from 'react';
import axios from 'axios';

const UsersPage: React.FC = () => {
  const [users, setUsers] = useState<any[]>([]);
  const [error, setError] = useState('');
  const [assigningRoleUserId, setAssigningRoleUserId] = useState<number | null>(null);
  const [selectedRole, setSelectedRole] = useState<string>('SUPPORT_ADMIN');

  const fetchUsers = async () => {
    try {
      const token = localStorage.getItem('token');
      const tenantId = localStorage.getItem('tenantId');
      const response = await axios.get(`http://localhost:8080/api/tenants/${tenantId}/users`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setUsers(response.data);
      setError('');
    } catch (err: any) {
      setError(err.response?.data || 'Failed to fetch users');
    }
  };

  const handleAssignRole = async (userId: number) => {
    try {
      const token = localStorage.getItem('token');
      console.log("Assigning Role - Token:", token);

      await axios.post(
        'http://localhost:8080/api/tenants/assign-role',
        {
          userId,
          role: selectedRole,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      alert(`Role ${selectedRole} assigned to user ID ${userId}`);
      fetchUsers();
      setAssigningRoleUserId(null); // reset
    } catch (err: any) {
      alert('Failed to assign role: ' + (err.response?.data || err.message));
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Users List</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <table border={1} cellPadding={10} cellSpacing={0}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Username</th>
            <th>Role</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.email}</td>
              <td>{user.username || '-'}</td>
              <td>
                {user.role ? (
                  user.role
                ) : assigningRoleUserId === user.id ? (
                  <>
                    <select
                      value={selectedRole}
                      onChange={e => setSelectedRole(e.target.value)}
                    >
                      <option value="SUPPORT_ADMIN">SUPPORT_ADMIN</option>
                      <option value="DEVOPS_ADMIN">DEVOPS_ADMIN</option>
                      <option value="BILLING_ADMIN">BILLING_ADMIN</option>
                      <option value="SYSTEM_ADMIN">SYSTEM_ADMIN</option>
                    </select>
                    <button
                      onClick={() => handleAssignRole(user.id)}
                      style={{ marginLeft: '8px' }}
                    >
                      ✅
                    </button>
                  </>
                ) : (
                  <button onClick={() => setAssigningRoleUserId(user.id)}>Assign</button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UsersPage;
