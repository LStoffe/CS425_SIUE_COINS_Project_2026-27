# COINS – Run with Docker (no Java/Maven/Node required)

Anyone can run the full app with **only Docker installed**. No need to install Java, Maven, Node, or set any paths.

## Quick Start

1. **Install Docker**  
   [Docker Desktop for Windows/Mac](https://www.docker.com/products/docker-desktop/) — includes Docker Compose.

2. **Clone the repo and navigate to the app folder**
```bash
   cd path/to/COINS-Project/Big70/BIG
```

3. **Start everything (first run)**
```bash
   docker compose build --no-cache
   docker compose up
```
   The first run will build the app image, initialize the database, and start all services. This may take a few minutes.

4. **Open the app**
   - App: **http://localhost:8080**
   - pgAdmin (DB UI): **http://localhost:5050**

5. **Default admin login**
   - Username: `admin`
   - Password: `admin1357`

> **Important:** Always use `docker compose build --no-cache` on first run to ensure the database is initialized correctly from the latest schema.

---

## What Runs in Docker

| Service | Port | Description |
|---------|------|-------------|
| **app** | 8080 | Spring Boot COINS app (built from this repo) |
| **db** | 5432 | PostgreSQL 16 (database: `bigdb`, user: `biguser`) |
| **pgadmin** | 5050 | Web UI for PostgreSQL |

The app container is built from the Dockerfile — Node for Tailwind CSS, Maven for Java, then JRE to run the jar. No local Java, Maven, or Node is required.

---

## Database Setup

The database is initialized automatically on first run using the SQL scripts in `docker-init-db/`:

- `01_schema.sql` — creates all tables and seeds default data
- `02_seed.sql` — creates the default admin account and PostgreSQL role

**No manual setup is required.** The default admin account is created automatically.

---

## Connecting pgAdmin to the Database

1. Open **http://localhost:5050**
2. Login with:
   - Email: `admin@coinsapp.dev`
   - Password: `adminpass`
3. Right-click **Servers → Register → Server...**
4. **General tab:**
   - Name: `COINS`
5. **Connection tab:**
   - Host: `db`
   - Port: `5432`
   - Maintenance Database: `bigdb`
   - Username: `biguser`
   - Password: `bigpass`
6. Click **Save**

---

## Useful Commands

| Task | Command |
|------|---------|
| First run (clean build) | `docker compose build --no-cache && docker compose up` |
| Start in background | `docker compose up -d` |
| View app logs | `docker logs -f big_app` |
| View DB logs | `docker logs -f big_postgres` |
| Stop all | `docker compose down` |
| Stop and wipe database | `docker compose down -v` |
| Rebuild after code changes | `docker compose up --build` |
| Full clean rebuild | `docker compose down -v && docker compose build --no-cache && docker compose up` |

---

## Troubleshooting

**Login says invalid username or password on first run:**
```bash
docker compose down -v
docker compose build --no-cache
docker compose up
```

**App won't start / port already in use:**  
Make sure ports 8080, 5432, and 5050 are not in use by another application.

**Changes to SQL files not taking effect:**  
Always use `--no-cache` when rebuilding after schema changes:
```bash
docker compose down -v
docker compose build --no-cache
docker compose up
```

---

## Local Development (Optional)

If you want to run the app locally with Java/Maven/Node instead of Docker, see `application.properties`. You will need:
- Java 21
- Maven 3.9+
- Node.js (for Tailwind CSS compilation)