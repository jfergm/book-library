# Book library

## Description

A Spring Boot REST API for managing a library system with JWT authentication, supporting book inventory and loan tracking.

## Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* Spring Security
* PostgreSQL
* Maven
* Docker

## Configuration

Configure the application in `src/main/resources/application.properties`:

```properties
jwt.secret=
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
```

Or when using Docker Compose:
```bash
cp .env.example .env
```
Then update the environment variables.

## Run with Docker Compose

```bash
docker compose up -d
```

Application URL:

```
http://localhost:8080
```


## Testing

The project includes:

* **Unit tests** using JUnit and Mockito
* **Integration tests** using an in-memory H2 database
* **Containerized integration tests** using Testcontainers (PostgreSQL in Docker)

Run all tests:

```bash
./mvnw verify
```

> **Note:** Testcontainers-based tests require Docker to be running.

## CI

![CI](https://github.com/jfergm/book-library/actions/workflows/maven.yml/badge.svg)

The project uses GitHub Actions to run the full Maven test suite (`mvn clean verify`) on every push and pull request using Java 21.
