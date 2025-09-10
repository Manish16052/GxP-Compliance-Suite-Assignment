const base = 'http://localhost:8080'
let jwt = ''

export function setToken(t){ jwt = t }

async function handle(res){
  if(!res.ok){
    const txt = await res.text()
    throw new Error(txt || res.statusText)
  }
  const ct = res.headers.get('content-type') || ''
  if (ct.includes('application/json')) return res.json()
  return res.text()
}

export const api = {
  async get(path, options={}){
    const params = options.params ? '?' + new URLSearchParams(options.params).toString() : ''
    const res = await fetch(base + path + params, { headers: jwt ? { Authorization: 'Bearer ' + jwt } : {} })
    return handle(res)
  },
  async post(path, body){
    const res = await fetch(base + path, {
      method:'POST',
      headers: { 'Content-Type':'application/json', ...(jwt ? { Authorization: 'Bearer ' + jwt } : {}) },
      body: JSON.stringify(body)
    })
    return handle(res)
  },
  async patch(path, body){
    const res = await fetch(base + path, {
      method:'PATCH',
      headers: { 'Content-Type':'application/json', ...(jwt ? { Authorization: 'Bearer ' + jwt } : {}) },
      body: JSON.stringify(body)
    })
    return handle(res)
  }
}
