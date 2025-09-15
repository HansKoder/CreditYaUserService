# User Service

## Overview
The **User Service** manages Customer who application submitted loans
It is responsible for:
- Create Customer (Advisor)
- Verify Customer (Customer)
- Get Customer By Document (Advisor)

This service is built following **Clean Architecture** principles to ensure testability, maintainability, and scalability.

---

## Tech Stack
- **Java 21**
- **Spring Boot 3.5**
- **Spring WebFlux** (Reactive stack)
- **Spring Security** (Security authorities)
- **JWT** (Validate Token)
- **Gradle** (build tool)
- **Docker** (containers)
- **cleanArchitectureVersion** (Clean Architecture Bancolombia's Open Source Tool) [Website, more information](https://bancolombia.github.io/scaffold-clean-architecture/docs/intro/)

---

## Architecture
- **Domain Layer**: Business rules (entities, value objects, contracts)
- **Application Layer**: Use cases (loan, loan review)
- **Infrastructure Layer**:
    - **Driven Adapters**:
        - Spring Security configuration
        - JWT token provider
        - Persistence adapters (R2DB)
    - **Entry Point**:
        - reactive-web (webflux)

---

## 🚀 Getting Started

### Prerequisites
- JDK 21+


### Create new image
```cmd
chmod +x build-image.sh
./setup.sh
```
