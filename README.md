# Freelance Platform

## Project Description and Motivation

The project aims to build a freelance platform using Spring Boot and other modern technologies. It facilitates seamless connections between freelancers and clients, offering various facilities for contract negotiation.

## Prerequisites

- Docker
- Java 11+
- Maven

## Installation and Setup

### Step 1: Clone the Repository

First, clone the repository to your local machine:

```bash
git clone https://gitlab.fel.cvut.cz/beliadan/nss_semesterwork.git
cd nss_semesterwork
```

### Step 2: Start Docker Containers

Navigate to the root directory of the project and start the Docker containers for the database and Kafka:

```bash
docker-compose -f compose.yaml up -d
```

This will set up and start the necessary services for the application.

### Step 3: Build and Run FreelanceApplication

Navigate to the FreelancePlatform directory, build the project using Maven, and start the application:

```bash
cd FreelancePlatform
mvn clean install -DskipTests
mvn spring-boot:run
```

### Step 4: Build and Run NotificationService

Navigate to the NotificationService directory, build the project using Maven, and start the application:

```bash
cd ../NotificationService
mvn clean install
mvn spring-boot:run
```

### Conclusion

Your freelance platform should now be up and running. You can access the services and start using the platform as intended.
