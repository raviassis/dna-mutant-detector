# DNA Mutant Detector

## Prerequisites

- Java 25
- Docker and Docker Compose

## Run In Terminal

1. Start PostgreSQL:

```bash
docker run --name magneto-db --rm -p 5432:5432 \
  -e POSTGRES_DB=magneto \
  -e POSTGRES_USER=magneto \
  -e POSTGRES_PASSWORD=magneto \
  postgres:16
```

2. In another terminal, run the application:

```bash
./mvnw quarkus:dev
```

The API will be available at:

- `http://localhost:8080/mutant`
- `http://localhost:8080/stats`

## Run With Docker Compose

From project root:

```bash
docker compose up --build
```

The API will be available at:

- `http://localhost:8080/mutant`
- `http://localhost:8080/stats`

Stop and remove containers:

```bash
docker compose down
```

## Run Tests

```bash
./mvnw test
```

## Check Test Coverage

JaCoCo report is generated during `test`.

1. Generate tests + coverage report:

```bash
./mvnw test
```

2. Open the HTML report:

- `target/site/jacoco/index.html`
