# Étape 1 : Builder l’application avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le pom.xml et télécharger les dépendances
COPY pom.xml .

# Copier le code source et construire le jar
COPY src ./src

# Étape 2 : Image finale (runtime)
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copier le JAR depuis l’image builder
COPY --from=builder /app/target/*.jar app.jar

# Exposer un port si ton appli en utilise un (par ex : 8080)
EXPOSE 8089

# Lancer ton application
ENTRYPOINT ["java", "-jar", "app.jar"]
