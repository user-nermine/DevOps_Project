# Étape 1 : Build avec Maven
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src


# (Optionnel) pour vérifier que le JAR a bien été créé
RUN echo "=== Contenu du dossier target ===" && ls -l /app/target

# Étape 2 : Image finale
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copier le JAR depuis l'étape build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
