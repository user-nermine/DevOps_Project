# Utilise OpenJDK 11 JRE slim pour exécuter le JAR
FROM eclipse-temurin:21-jdk-jammy AS builder

# Variable pour le JAR généré par Maven
ARG JAR_FILE=target/*.jar

# Copier le JAR dans l'image
COPY ${JAR_FILE} app.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8089

# Commande pour lancer le JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
