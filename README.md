# Java System Aggregator Microservice
**Author:** Alexander Kuziv  
**Project:** <a href="https://github.com/makklays/secure-credit-hub">Secure Credit Hub</a>

## Overview
The **Java System Aggregator Microservice** acts as the high-performance data-orchestration engine within the **Secure Credit Hub** ecosystem. Positioned directly behind the API Gateway, its primary responsibility is to fetch, aggregate, transform, and normalize scattered financial, currency, and user data from multiple isolated domain microservices.

By utilizing non-blocking, reactive streams and orchestrating distributed sagas, this service synthesizes composite data models and feeds them directly back to the Gateway or front-facing clients, eliminating client-side data stitching and drastically minimizing network overhead.

---

## Features
* **Reactive Data Aggregation:** Uses non-blocking I/O to hit multiple downstream microservices concurrently, merging their JSON responses asynchronously into optimized composite payloads.
* **Orchestration-Based Saga Management:** Safely coordinates multi-step data mutation and synchronization flows across isolated service domains, ensuring eventual consistency with an elite fallback and automated rollback engine.
* **Fault Tolerance & Cascade Isolation:** Fully weaponized with **Resilience4j** protective patterns (`Circuit Breaker`, `Retry`, `Bulkhead`, `Rate Limiter`) to intercept temporary network hiccups and isolate downstream service failures.
* **Distributed Logging Tracing:** Enforces zero-leak context propagation by scanning and passing a universal `X-Correlation-ID` header across all reactive threads and HTTP channels.
* **Prometheus Metrics Production:** Built-in instrumentation tracking thread performance, system health, and reactive flow rates ready to be scraped by monitoring clusters.

---

## Technologies Used
* **Core Language:** Java 17 (leveraging Record classes, Pattern Matching, and Clean Code principles).
* **Framework:** Spring Boot 3.3.7.
* **Reactive Engine:** Spring WebFlux & Project Reactor (Fully non-blocking asynchronous event-loop architecture).
* **Resilience Layer:** Resilience4j 2.0.2 (AOP-based Circuit Breakers, Retries, and Rate Limiters).
* **HTTP Client:** WebClient (Reactive, thread-safe, non-blocking target calls).
* **Observability:** Spring Boot Actuator & Micrometer Registry Prometheus.
* **Build System:** Apache Maven 3.x (Modular dependency and plugin pipeline).

---

## Getting Started

### Prerequisites
Before running or building this microservice, ensure your local development environment meets the following requirements:

* **Java Development Kit (JDK):** Version 17 or higher installed (`java -version`).
* **Apache Maven:** Version 3.8.x or higher configured (`mvn -version`).
* **Docker & Docker Compose:** Required to run target microservices and infrastructure (PostgreSQL, Kafka, Prometheus).
* **Network Connectivity:** Access to external repositories to resolve dependencies defined in the project descriptor (`POM.xml`).

### Compilation & Build
To clean compilation caches and package the microservice into an executable JAR file, run:
```bash
mvn clean package
```
*Note: If you need to quickly package the application bypassing unit verification layers, append `-DskipTests`.*

### Running the Application
To launch the reactive aggregator engine locally in development mode via Maven, run:
```bash
mvn spring-boot:run
```

