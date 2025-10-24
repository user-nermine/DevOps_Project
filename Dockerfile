# build stage (optionnel si tu builds localement)
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src
RUN mvn -B -DskipTests package

# runtime stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# copie le jar produit (adapter le nom si nécessaire)
COPY --from=build /workspace/target/orderapp.jar /app/orderapp.jar

# si tu veux lancer en headless javaFX (si tu as besoin):
# ENV JAVA_OPTS="-Djava.awt.headless=true"

EXPOSE 8089
ENTRYPOINT ["java","-jar","/app/orderapp.jar"]
