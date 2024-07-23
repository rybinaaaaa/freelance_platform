FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -Dmaven.test.skip=true
RUN ls -l /app/target

FROM openjdk:23-ea-jdk-oracle
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "app.jar"]