import React, { useEffect, useState } from 'react'
import { api, setToken } from './api'

export default function App() {
  const [docs, setDocs] = useState([])
  const [statusFilter, setStatusFilter] = useState('')
  const [token, setTok] = useState('')
  const [form, setForm] = useState({ title: '', type: '' })
  const [role, setRole] = useState('USER')
  const [login, setLogin] = useState({username:'user1', password:'password'})

  const load = async () => {
    const res = await api.get('/api/documents', statusFilter ? { params: { status: statusFilter }} : {})
    setDocs(res)
  }

  const registerDefault = async () => {
    try {
      await api.post('/api/auth/register', {username: 'admin', password: 'admin', role: 'ADMIN'})
      await api.post('/api/auth/register', {username: 'user1', password: 'password', role: 'USER'})
      alert('Seed users created')
    } catch(e) { console.log(e) }
  }

  const doLogin = async () => {
    const res = await api.post('/api/auth/login', login)
    setTok(res.token)
    setToken(res.token)
    alert('Login success')
  }

  const upload = async () => {
    await api.post('/api/documents', {...form, uploadedBy: login.username})
    setForm({ title: '', type: '' })
    await load()
  }

  const approve = async (id, status) => {
    await api.patch(`/api/documents/${id}/status`, { status })
    await load()
  }

  useEffect(() => { if(token) load() }, [token, statusFilter])

  return (
    <div style={{maxWidth: 900, margin: '20px auto', fontFamily: 'Inter, system-ui'}}>
      <h1>GxP Compliance Dashboard</h1>
      <section style={{display:'grid', gridTemplateColumns:'1fr 1fr', gap:16}}>
        <div style={{border:'1px solid #ddd', padding:16, borderRadius:12}}>
          <h3>Auth</h3>
          <button onClick={registerDefault}>Seed Admin/User</button>
          <div style={{marginTop:8}}>
            <input placeholder="username" value={login.username} onChange={e=>setLogin({...login, username:e.target.value})} />
            <input placeholder="password" type="password" value={login.password} onChange={e=>setLogin({...login, password:e.target.value})} />
            <button onClick={doLogin}>Login & Get JWT</button>
          </div>
          <div><small>Token: {token ? token.slice(0,20)+'...' : '—'}</small></div>
        </div>

        <div style={{border:'1px solid #ddd', padding:16, borderRadius:12}}>
          <h3>Upload Document</h3>
          <input placeholder="Title" value={form.title} onChange={e=>setForm({...form, title:e.target.value})} />
          <input placeholder="Type" value={form.type} onChange={e=>setForm({...form, type:e.target.value})} />
          <button onClick={upload} disabled={!token}>Upload</button>
        </div>
      </section>

      <section style={{marginTop:24, border:'1px solid #ddd', padding:16, borderRadius:12}}>
        <h3>Documents</h3>
        <select value={statusFilter} onChange={e=>setStatusFilter(e.target.value)}>
          <option value="">All</option>
          <option value="PENDING">PENDING</option>
          <option value="APPROVED">APPROVED</option>
          <option value="REJECTED">REJECTED</option>
        </select>
        <table width="100%" cellPadding="8" style={{marginTop:8, borderCollapse:'collapse'}}>
          <thead><tr><th>ID</th><th>Title</th><th>Type</th><th>Status</th><th>Actions (Admin)</th></tr></thead>
          <tbody>
            {docs.map(d => (
              <tr key={d.documentId} style={{borderTop:'1px solid #eee'}}>
                <td>{d.documentId}</td>
                <td>{d.title}</td>
                <td>{d.type}</td>
                <td>{d.status}</td>
                <td>
                  <button onClick={()=>approve(d.documentId, 'APPROVED')} disabled={!token}>Approve</button>
                  <button onClick={()=>approve(d.documentId, 'REJECTED')} disabled={!token}>Reject</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  )
}
