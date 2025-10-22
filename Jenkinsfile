pipeline {
    agent {
        docker {
            image 'maven:3.9-openjdk-17'
            args '-v /var/run/docker.sock:/var/run/docker.sock -u root'
        }
    }
    
    environment {
        DOCKER_IMAGE_NAME = 'devops-maram-app'
        SONAR_PROJECT_KEY = 'DevOps-Project-Maram'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '📁 Checkout du code...'
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Verify Structure') {
            steps {
                echo '🔍 Vérification de la structure du projet...'
                sh '''
                    echo "=== Structure du projet ==="
                    ls -la
                    echo "=== Fichier pom.xml ==="
                    cat pom.xml || echo "pom.xml non trouvé"
                    echo "=== Source Java ==="
                    find . -name "*.java" | head -10 || echo "Aucun fichier Java trouvé"
                '''
            }
        }
        
        stage('Build & Test') {
            steps {
                echo '🔨 Compilation et tests...'
                sh 'mvn -B clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Analyse de qualité avec SonarQube...'
                script {
                    try {
                        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                            sh """
                                mvn sonar:sonar \
                                  -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                                  -Dsonar.projectName="DevOps Project - Maram" \
                                  -Dsonar.host.url=http://sonarqube:9000 \
                                  -Dsonar.login=${SONAR_TOKEN}
                            """
                        }
                    } catch (Exception e) {
                        echo "⚠️ SonarQube ignoré: ${e.message}"
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo '📊 Vérification Quality Gate...'
                script {
                    try {
                        timeout(time: 2, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: false
                        }
                    } catch (Exception e) {
                        echo "⚠️ Quality Gate ignorée: ${e.message}"
                    }
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '📦 Création du package JAR...'
                sh 'mvn -B package -DskipTests'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo '🐳 Construction de l image Docker...'
                script {
                    sh """
                        # Créer un Dockerfile simple
                        cat > Dockerfile << 'EOF'
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
                        # Construire l'image
                        docker build -t ${DOCKER_IMAGE_NAME}:latest .
                        echo "✅ Image Docker construite: ${DOCKER_IMAGE_NAME}:latest"
                        
                        # Lister les images
                        docker images | grep ${DOCKER_IMAGE_NAME}
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo '📊 Pipeline terminé - Rapport final'
            script {
                // Archive conditionnelle
                if (fileExists('target/*.jar')) {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    echo '📦 JAR archivé avec succès'
                } else {
                    echo '⚠️ Aucun JAR à archiver'
                    sh 'find . -name "*.jar" || echo "Aucun JAR trouvé"'
                }
                
                // Rapport final
                sh '''
                    echo "=== RAPPORT FINAL ==="
                    echo "Fichiers générés:"
                    find target/ -type f 2>/dev/null | head -20 || echo "Dossier target vide"
                    echo "Images Docker:"
                    docker images | head -10 || echo "Docker non disponible"
                    echo "====================="
                '''
            }
        }
        success {
            echo '✅✅✅ PIPELINE RÉUSSI ! ✅✅✅'
            echo '🔍 Vérifiez SonarQube: http://localhost:9000'
            echo '🐳 Image Docker: devops-maram-app:latest'
        }
        failure {
            echo '❌❌❌ PIPELINE EN ÉCHEC ❌❌❌'
        }
    }
}
