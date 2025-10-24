# Use an official OpenJDK 21 image as the base
FROM openjdk:21-jdk-slim

# Define an argument for the JAR file name (optional, but good practice)
ARG JAR_FILE=target/*.jar

# Copy the application's JAR file into the container
COPY ${JAR_FILE} app.jar

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
