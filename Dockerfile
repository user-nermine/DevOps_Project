# Étape 1 : Builder avec Maven
FROM maven:3.9.2-eclipse-temurin-21 AS builder
WORKDIR /app

# Copier le pom et le code source
COPY pom.xml .
COPY src ./src

# Compiler le projet et créer le JAR
RUN mvn clean package -DskipTests

# Étape 2 : Créer l'image finale plus légère
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copier le JAR depuis l'étape builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app.jar"]
