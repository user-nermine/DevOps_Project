# Étape 1 : Utiliser une image Maven avec Java 21
FROM maven:3.9.6-eclipse-temurin-21

# Définir le répertoire de travail
WORKDIR /app

# Copier tout le projet dans le conteneur
COPY . .


# Exposer le port utilisé par ton application
EXPOSE 8089

# Lancer l’application
CMD ["java", "-jar", "target/app.jar"]
