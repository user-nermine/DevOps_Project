FROM alpine:latest

# Installer Java 17
RUN apk add --no-cache openjdk17

# Créer un dossier de travail
WORKDIR /app

# Copier le fichier .jar dans l'image Docker
COPY target/*.jar app.jar

# Exposer un port (si ton appli écoute sur un port, sinon optionnel)
EXPOSE 8080

# Lancer l'application Java
CMD ["java", "-jar", "app.jar"]
