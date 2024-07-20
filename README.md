# Freelance Platform

## Project Description and Motivation

The project aims to build a freelance platform using Spring Boot and other modern technologies. It facilitates seamless connections between freelancers and clients, offering various facilities for contract negotiation.

## Documentation
https://docs.google.com/document/d/14QOzUKwhIsbmJxZe33ymWSRt-85Maz07XX8YSVdFAus/edit?usp=sharing

## Requires done
- Java/SpringBoot/React
- Git
- DB Postgres
- Cache Hazelcast
- Kafka
- Authorization base64
- REST
- Deploy on Render
- Event base (notifications)
- Instruction
- Patterns

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

Open another terminal and navigate to the NotificationService directory, build the project using Maven, and start the application:

```bash
cd nss_semesterwork/NotificationService
mvn clean install
mvn spring-boot:run
```

### Step 5: Build and Run Front-end
1. [Download and install](https://nodejs.org/en/) the latest LTS version of Node.js.
2. Run `node -v` in the console and make sure that the installed Node.js version is not lower than v8.11.3.;
3. Run `npm -v` in the console and make sure that the installed npm version is not lower than 5.6.0.
4. Clone and start this project: `git clone bb599884b29b0baa3d7088fd402023959583fde8`;
5. Switch to the filonole branch by running `git checkout filonole`.
7. Navigate to the project directory after cloning by running the command: `cd freelance_platform\nssfrontend`.;
8. To install the project dependencies, run the command `npm install`.
9. Start Zookeeper `.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`
10. Start Kafka `.\bin\windows\kafka-server-start.bat .\config\server.properties`
11. To start the project in development mode, run the command `npm run start`.
12. Open your browser and navigate to http://localhost:3000. Once the page loads, you will see a page."
13. Open Chrome Dev Tools and navigate to the Console tab; there should be no errors.

### Conclusion

Your freelance platform should now be up and running. You can access the services and start using the platform as intended.

## Deploy on production server

Here you can explore our web service:

...

Note: at the moment, not all the functionality of the application works on the product server


## Patterns

In this project, we have applied the following patterns:

- DTO
- MVC
- Dependency injection
- Strategy - path: notificationService/notificationStrategies
- Builder - Mapper.userDTOToUser() method
- Modular approach - frontend with React