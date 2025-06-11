# Use a lightweight Java 17 image
#FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
#WORKDIR /app

# Copy the JAR file from the build context
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar

# Run the application
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/CarService-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

