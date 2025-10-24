# Étape 1 : Build Maven
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler et packager l'application
RUN mvn compile -DskipTests

# Étape 2 : Image finale Java
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copier le JAR généré depuis l’étape précédente
COPY --from=build /workspace/target/*.jar app.jar

# Exposer le port de ton application
EXPOSE 8089

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
