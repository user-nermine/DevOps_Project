# --- STAGE 1: Build the application ---
FROM maven:3.9.8-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src ./src

# Build the project, creating the target directory and JAR
RUN mvn clean package -DskipTests

# --- STAGE 2: Create the final image ---
# Use a minimal runtime image for the final, lean package
FROM openjdk:21-jre-slim

# Set the final image's working directory
WORKDIR /app

# Copy the JAR from the build stage into the final image
COPY --from=build /app/target/*.jar app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
