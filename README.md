# Shop App

A RESTful e-commerce backend built with **Java 17 and Spring Boot**. The project was developed to gain practical experience designing, implementing, securing, testing, and running a backend application from scratch.

The application provides user authentication, role-based authorization, product management, customer profiles, and shopping cart functionality.

## Features

* JWT-based authentication with access and refresh tokens
* Role-based access control with `USER` and `ADMIN` roles
* BCrypt password hashing
* Product management
* Customer profile management
* Shopping cart management
* Persistent data storage with MySQL
* JPA/Hibernate for database access
* Centralized exception handling
* Unit testing with JUnit and Mockito
* Docker Compose for running MySQL
* OpenAPI documentation with Swagger UI
* Layered application architecture
* UML-based domain model design

## Tech Stack

* **Java 17**
* **Spring Boot 3**
* **Spring Web**
* **Spring Security**
* **JWT**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Maven**
* **Docker / Docker Compose**
* **JUnit**
* **Mockito**
* **OpenAPI / Swagger UI**

## Architecture

The application follows a layered architecture that separates HTTP handling, business logic, and data access.

```text
┌─────────────────────┐
│       Client        │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│    Controllers      │
│      REST API       │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│      Services       │
│   Business Logic    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│    Repositories     │
│     Data Access     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│        MySQL        │
│      Database       │
└─────────────────────┘
```

## Authentication & Authorization

Authentication is implemented using **Spring Security and JWT**.

The authentication flow consists of:

1. User creates an account.
2. User authenticates with their credentials.
3. The server returns an access token and refresh token.
4. The access token is sent with protected requests using the `Authorization` header.
5. `JwtAuthFilter` validates the token and establishes the authenticated user.
6. Role-based authorization determines whether the user can access protected operations.

The application uses two roles:

The `ADMIN` role inherits the permissions of `USER`.

Administrative operations such as creating and deleting products are restricted using method-level authorization.

## API Documentation

The REST API is documented using **OpenAPI 3 and Swagger UI**.

After starting the application, interactive API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The generated OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

## Domain Model

Relationships between the entities are documented using UML.

The project contains the domain model documentation in:

```text
src/main/resources/uml.MD
```

## Testing

The project uses **JUnit** and **Mockito** to test application logic while isolating services from external dependencies.

Run the tests with:

```bash
./mvnw test
```

## Getting Started

### Prerequisites

Make sure you have the following installed:

* Java 17+
* Docker
* Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/HlibShutov/spring_shop.git
cd spring_shop
```

### 2. Start MySQL

The project includes a Docker Compose configuration for the MySQL database.

```bash
docker compose up -d
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

### 4. Open Swagger UI

Once the application is running, open:

```text
http://localhost:8080/swagger-ui/index.html
```

From there you can explore and test the API directly from your browser.

### 5. Stop MySQL

```bash
docker compose down
```

## Project Goals

The main goal of this project was to gain practical experience building a backend application using the Spring ecosystem.

Through this project I practiced:

* Designing a layered backend architecture
* Building RESTful APIs with Spring Boot
* Implementing JWT authentication
* Configuring Spring Security
* Implementing role-based authorization
* Working with JPA and Hibernate
* Designing relational database models
* Handling application-specific exceptions
* Writing unit tests with Mockito
* Running application dependencies using Docker Compose
* Documenting REST APIs using OpenAPI and Swagger
* Designing and documenting the domain model with UML