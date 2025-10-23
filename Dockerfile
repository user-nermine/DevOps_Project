# Stage 1: Build the application into a "fat" or shaded JAR.
# A fat JAR includes all dependencies, including JavaFX modules, so the final image doesn't need them.
FROM eclipse-temurin:21-jdk-jammy AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src src/

# Build the fat JAR. This requires a plugin like maven-shade-plugin to bundle all dependencies.
# You need to add the maven-shade-plugin to your pom.xml for this step to work.
RUN --mount=type=cache,target=/root/.m2 mvn clean package

# Stage 2: Create the final, lightweight runtime image
FROM eclipse-temurin:21-jre-jammy

# Workaround for JavaFX applications in containers that require graphical capabilities
# Install graphical libraries needed by JavaFX
RUN apt-get update && apt-get install -y libglib2.0-0 libgtk-3-0 libxtst6 libxrender1
# Disable headless mode for JavaFX
ENV JAVA_TOOL_OPTIONS="-Djava.awt.headless=false"

# Define the user to run the application (best practice)
RUN groupadd --system springboot && useradd --system --gid springboot springboot
USER springboot

# Copy the fat JAR from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Set the entrypoint to run the JavaFX application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]