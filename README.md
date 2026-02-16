# DNA Mutant Detector

## Run all projects with Docker Compose

From the project root:

```bash
docker compose up -d
```

This starts:

| Service   | URL                    | Description        |
|----------|------------------------|--------------------|
| Frontend | http://localhost:3000  | Next.js app        |
| Backend  | http://localhost:8080  | Quarkus API        |
| Database | localhost:5432         | PostgreSQL (magneto)|

To run in the foreground (logs in the terminal):

```bash
docker compose up
```

To stop and remove containers:

```bash
docker compose down
```

To rebuild images after code changes:

```bash
docker compose up -d --build
```
