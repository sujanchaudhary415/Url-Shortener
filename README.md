# URL Shortener

A backend URL Shortener application built using Java and Spring Boot. 
It converts long URLs into short, unique links and tracks click analytics 
using asynchronous messaging.

## Features

- Create unique short URLs
- Redirect users to original URLs
- Track click analytics
- Asynchronous click processing using RabbitMQ
- Redis caching for faster URL lookups
- Paginated analytics
- PostgreSQL database persistence
- Retry mechanism and Dead Letter Queue
- REST API documentation using Swagger
- Docker Compose support

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4 | Backend framework |
| Spring Data JPA | Database operations |
| PostgreSQL | Persistent database |
| Redis | URL caching |
| RabbitMQ | Asynchronous click event processing |
| Flyway | Database migrations |
| Maven | Build and dependency management |
| JUnit 5 & Mockito | Unit testing |
| Docker & Docker Compose | Containerization |
| Swagger / OpenAPI | API documentation |
| Postman | API testing |

## System Architecture

The application uses Redis for caching frequently accessed URLs and RabbitMQ for asynchronous click event processing.

```mermaid
flowchart TD
    A[Client] --> B[Spring Boot Application]

    B --> C[Redis Cache]
    C -->|Cache Miss| D[PostgreSQL]
    D --> C

    B -->|Redirect| A

    B -->|Publish Click Event| E[RabbitMQ]
    E --> F[Click Event Consumer]
    F --> G[PostgreSQL Click Analytics]
