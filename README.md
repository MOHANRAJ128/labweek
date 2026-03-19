# Labweek - CodeInsight AI

End-to-end AI-powered Java error analysis platform with:
- Angular frontend
- Spring Boot backend (multi-module Maven)
- PostgreSQL
- Ollama (llama3.2)

The recommended way to run the full stack is Docker Compose.

## Project Structure

- `bug-analyzer/codeinsight-ui` - Angular app
- `bug-analyzer/codeinsight-ai` - Spring Boot backend modules
- `docker-compose.yml` - Full stack orchestration

## Prerequisites

### For Docker-based run (recommended)

- Docker Desktop (or Docker Engine + Compose plugin)
- Minimum suggested RAM: 12 GB (Ollama model loading can be heavy)
- Free disk space: at least 8 GB

### For local non-Docker run (optional)

- Java 21
- Maven 3.9+
- Node.js 20+
- npm 10+
- PostgreSQL 16+
- Ollama installed locally
- Ollama model downloaded: `llama3.2`

## Quick Start (Docker Compose)

Run from repository root:

```bash
docker compose up --build -d
```

Check service status:

```bash
docker compose ps
```

Follow logs:

```bash
docker compose logs -f
```

Open apps:
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080
- Ollama API: http://localhost:11434

Stop all services:

```bash
docker compose down
```

Stop and remove volumes too (clean reset):

```bash
docker compose down -v
```

## Environment Variables

Current runtime values are defined in `docker-compose.yml`.

### PostgreSQL

- `POSTGRES_USER=postgres`
- `POSTGRES_PASSWORD=password`
- `POSTGRES_DB=codeinsight_ai`
- `TZ=Asia/Kolkata`

### Backend (Spring Boot)

- `TZ=Asia/Kolkata`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/codeinsight_ai`
- `SPRING_DATASOURCE_USERNAME=postgres`
- `SPRING_DATASOURCE_PASSWORD=password`
- `LANGCHAIN4J_OLLAMA_CHAT_MODEL_BASE_URL=http://ollama:11434`
- `LANGCHAIN4J_OLLAMA_CHAT_MODEL_MODEL_NAME=llama3.2`

### Ollama

- `OLLAMA_HOST=http://ollama:11434` (used by model pull job)

## What Docker Compose Starts

1. `postgres` - database
2. `ollama` - LLM runtime
3. `ollama-pull` - one-time pull for `llama3.2`
4. `backend` - Spring Boot API
5. `frontend` - Angular app served via nginx

## API and Upload Flow

Frontend calls:
- `POST /ai/debug`

Upload requirements in UI:
- Project file must be `.zip`
- Error log file must be `.txt`

## Local Development (Without Docker)

### 1) Start PostgreSQL

Create DB:
- Database: `codeinsight_ai`
- User/password matching `application.yml` (or update config)

### 2) Start Ollama + model

```bash
ollama serve
ollama pull llama3.2
```

### 3) Run backend

```bash
cd bug-analyzer/codeinsight-ai
mvn clean spring-boot:run -pl app -am
```

Backend starts on http://localhost:8080

### 4) Run frontend

```bash
cd bug-analyzer/codeinsight-ui
npm install
npm start
```

Frontend starts on http://localhost:4200

Note:
- In Docker mode, frontend uses nginx proxy and relative API paths.
- For pure local mode with `ng serve`, if API calls fail, set backend URL in `file-upload.service.ts` as needed.

## Useful Docker Commands

Restart all services:

```bash
docker compose restart
```

Restart specific services:

```bash
docker compose restart backend frontend
```

See a service log:

```bash
docker logs -f codeinsight-backend
docker logs -f codeinsight-frontend
docker logs -f codeinsight-ollama
```

## Troubleshooting

- If Ollama is slow on first run: model download can take time; watch `ollama-pull` logs.
- If backend fails DB connection: verify `postgres` is healthy and credentials match.
- If frontend cannot reach backend in Docker: ensure frontend container is up and nginx proxy config is loaded.
- If port conflicts occur: change host-side ports in `docker-compose.yml`.
