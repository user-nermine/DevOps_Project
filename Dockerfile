# Étape 1 : Build avec Maven
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

# Copier le pom.xml d'abord pour le cache Docker
COPY pom.xml .



# Copier le code source
COPY src ./src

# Compiler et packager le projet
RUN mvn clean package -DskipTests

# Étape 2 : Image finale
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copier le JAR depuis l'étape build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8089

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
