# GxP Compliance Suite (QMS/DMS/TMS sample)

Full-stack sample implementing:
- Spring Boot + MySQL
- JWT auth (Spring Security)
- Role-based access (USER/ADMIN)
- Document CRUD + status update
- Mock notification service with Resilience4j (circuit breaker + retry)
- React dashboard (upload/list/approve)
- Dockerfiles for backend & frontend

## Quick Start (Local)
1) Start MySQL locally and update `application.yml` credentials if needed.
2) Build & run backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
3) Start frontend:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
4) Open http://localhost:5173

### Auth
- Click "Seed Admin/User" to create:
  - admin / admin (ADMIN)
  - user1 / password (USER)
- Login to get JWT, then upload or approve.

### API Endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/documents?status=PENDING`
- `POST /api/documents` (USER/ADMIN)
- `PATCH /api/documents/{id}/status` (ADMIN)

### Docker (optional)
You can containerize each app independently using their Dockerfiles.
