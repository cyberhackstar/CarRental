# Use a lightweight Java 17 image
#FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
#WORKDIR /app

# Copy the JAR file from the build context
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar

# Run the application
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/CarService-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

