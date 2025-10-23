# Étape 1 : Builder le JAR avec Maven
FROM maven:3.9.2-eclipse-temurin-21 AS build

# Copier le projet Maven
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build du JAR exécutable avec toutes les dépendances (JavaFX inclus)
RUN mvn clean package -DskipTests

# Étape 2 : Image finale pour exécuter le JAR
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copier le JAR depuis l’étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port si besoin (ex: backend REST)
EXPOSE 8089

# Commande pour lancer l’application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
