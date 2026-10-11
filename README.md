# JobLens

JobLens is an AI-powered job discovery and resume intelligence platform.

## MVP scope

- JWT authentication and candidate profiles
- Job ingestion-ready data model with search, filtering, and pagination
- Resume upload pipeline with structured extraction boundaries
- Transparent resume-to-job matching with matched skills, missing requirements, and evidence
- Candidate-specific job recommendations ranked by explainable match score
- Saved jobs and application tracking
- MySQL for transactional data and MongoDB for resume documents

## Repository layout

```text
backend/    Spring Boot REST API
frontend/   React + Vite client
infra/      Local MySQL and MongoDB setup
docs/       Architecture and API notes
```

## Local prerequisites

- Java 21
- Maven 3.9+
- Node.js 20+
- Docker and Docker Compose

## Start dependencies

```bash
docker compose -f infra/docker-compose.yml up -d
```

## Planned implementation order

1. Authentication, profiles, jobs, saved jobs, and applications
2. Resume text extraction and structured profile persistence
3. Explainable skill matching and ranking evaluation
4. Hybrid search and RAG assistant
5. Background processing, observability, CI, and production deployment

The project intentionally avoids claiming hiring-probability predictions. Match scores describe evidence alignment between a candidate profile and a job description.

## Run locally

Start the databases:

```bash
docker compose -f infra/docker-compose.yml up -d
```

Start the API from `backend`:

```bash
mvn spring-boot:run
```

Start the frontend from `frontend`:

```bash
npm install
npm run dev
```

The frontend runs at `http://localhost:5173` and the API at `http://localhost:8080`.

## Current API capabilities

- `/api/auth/register`, `/api/auth/login`
- `/api/profile`
- `/api/jobs`
- `/api/saved-jobs`
- `/api/applications`
- `/api/resumes`
- `/api/resumes/latest/profile`
- `/api/jobs/{id}/match`
- `/api/recommendations`
- `/api/assistant/ask`
- `/api/evaluation/ranking`

Interactive API documentation is available at `http://localhost:8080/swagger-ui.html` when the backend is running. The OpenAPI JSON specification is available at `/v3/api-docs`.

## CI and health check

GitHub Actions runs backend tests and the frontend production build on pushes and pull requests. The API exposes `GET /api/health` for deployment checks and logs request method, path, status, duration, and client address in structured key-value form.

Authentication and assistant routes are protected by a simple per-IP in-memory rate limit. For a multi-instance production deployment, replace this with a shared store such as Redis or an API gateway limit.

The current release is an MVP development build. Production deployment still requires hosted databases, secret management, migrations, observability, and CI verification.

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for system design and [docs/DEMO_SCRIPT.md](docs/DEMO_SCRIPT.md) for a complete project demonstration flow.

## Vercel frontend deployment

The included root `vercel.json` allows deployment from the repository root. In Vercel, use:

```text
Framework: Vite
Build command: cd frontend && npm install && npm run build
Output directory: frontend/dist
```

Set `VITE_API_URL` to the deployed Spring Boot API URL ending in `/api`. Set `FRONTEND_ORIGIN` on the backend to the Vercel deployment URL.
