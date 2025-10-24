# Étape 1 : Build du JAR
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src


# Étape 2 : Exécution du JAR
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]
