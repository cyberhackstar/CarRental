 # Stage 1: Build the application
# FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests

# # Stage 2: Run the application
# FROM eclipse-temurin:21-jdk-alpine
# WORKDIR /app
# COPY --from=build /app/target/CarService-0.0.1-SNAPSHOT.jar app.jar
# EXPOSE 8081
# ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application with Nginx and Certbot
FROM alpine:latest
RUN apk add --no-cache openjdk21 nginx certbot certbot-nginx bash

WORKDIR /app

# Copy Spring Boot JAR
COPY --from=build /app/target/CarService-0.0.1-SNAPSHOT.jar app.jar

# Copy Nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf


# Create directories for Certbot
RUN mkdir -p /var/www/certbot /etc/letsencrypt

# Expose ports
EXPOSE 80
EXPOSE 443

# Start Nginx and Spring Boot app
CMD nginx && java -jar /app/app.jar
