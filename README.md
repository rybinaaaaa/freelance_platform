# Freelance Platform

## Project Description and Motivation

The project aims to build a freelance platform using Spring Boot and other modern technologies. It facilitates seamless connections between freelancers and clients, offering various facilities for contract negotiation.

## Documentation
https://docs.google.com/document/d/14QOzUKwhIsbmJxZe33ymWSRt-85Maz07XX8YSVdFAus/edit?usp=sharing

## Requires
- Java/SpringBoot - yes
- Git - yes
- DB Postgres - yes
- Cache Hazelcast - yes
- Kafka - yes
- Authorization base64 - yes
- Interceptors - yes
- REST - yes
- Deploy on Render - yes
- Vhodna architektura - yes, event base (notifications)
- Instruction - yes
- Elasticsearch - no
- Patterns - yes
- Cloud services - no

## Prerequisites

- Docker
- Java 11+
- Maven

## Installation and Setup

### Step 1: Clone the Repository

First, clone the repository to your local machine:

```bash
git clone https://gitlab.fel.cvut.cz/beliadan/freelance_platform.git
cd freelance_platform
```

### Step 2: Start Docker Containers

Navigate to the root directory of the project and start the Docker containers with the database, kafka, notification service and application platform:

```bash
docker-compose -f compose.yaml up -d
```

This will set up and start the necessary services and application.

### Step 3: Build and Run Front-end
1. [Download and install](https://nodejs.org/en/) the latest LTS version of Node.js.
2. Run `node -v` in the console and make sure that the installed Node.js version is not lower than v8.11.3.;
3. Run `npm -v` in the console and make sure that the installed npm version is not lower than 5.6.0.
4. Navigate to the project directory after cloning by running the command: `cd freelance_platform\nssfrontend`.;
5. To install the project dependencies, run the command `npm install`.
6. To start the project in development mode, run the command `npm run start`.
7. Open your browser and navigate to http://localhost:3000. Once the page loads, you will see a page."
8. Open Chrome Dev Tools and navigate to the Console tab; there should be no errors.

### Conclusion

Your freelance platform should now be up and running. You can access the services and start using the platform as intended.

## Deploy on production server

Here you can explore our web service:

backend: https://freelance-platform-3-0-2.onrender.com

frontend: https://shestser.github.io/nssSp/

Note: at the moment, not all the functionality of the application works on the product server


## Patterns

In this project, we have applied the following patterns:

- DTO
- Dependency injection
- Factory - path: notificationService/topics
- Strategy - path: notificationService/notificationStrategies
- Builder - Mapper.userDTOToUser() method
- Modular approach - frontend with React
