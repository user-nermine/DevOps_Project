# Étape 1 : build avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Étape 2 : image runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Exposer le port utilisé par l'application
EXPOSE 8089

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
