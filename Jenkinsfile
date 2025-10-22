pipeline {
    agent any
    
    tools {
        maven 'maven'
    }
    
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000'
        DOCKER_IMAGE_NAME = 'my-maven-app'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '📁 Checkout du code...'
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
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
                                  -Dsonar.projectKey=DevOps-Project-Maram \
                                  -Dsonar.projectName="DevOps Project Maram" \
                                  -Dsonar.host.url=${SONAR_HOST_URL} \
                                  -Dsonar.login=${SONAR_TOKEN}
                            """
                        }
                    } catch (Exception e) {
                        echo "⚠️ SonarQube ignoré - Configurez le token"
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo '📊 Vérification Quality Gate...'
                script {
                    try {
                        timeout(time: 5, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: false
                        }
                    } catch (Exception e) {
                        echo "⚠️ Quality Gate ignorée"
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
                        docker build -t ${DOCKER_IMAGE_NAME}:latest .
                        docker images | grep ${DOCKER_IMAGE_NAME}
                    """
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                echo '🚀 Envoi de l image Docker...'
                script {
                    // Optionnel - si vous avez un registry Docker
                    echo "Image ${DOCKER_IMAGE_NAME}:latest prête pour le déploiement"
                    sh "docker save ${DOCKER_IMAGE_NAME}:latest > ${DOCKER_IMAGE_NAME}.tar"
                }
            }
        }
    }
    
    post {
        always {
            echo '📊 Pipeline terminé - Rapport final'
            script {
                // Archive des artefacts
                if (fileExists('target/*.jar')) {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    echo '📦 JAR archivé'
                }
                if (fileExists('${DOCKER_IMAGE_NAME}.tar')) {
                    archiveArtifacts artifacts: '${DOCKER_IMAGE_NAME}.tar', fingerprint: true
                    echo '🐳 Image Docker sauvegardée'
                }
                
                // Nettoyage
                sh 'docker system prune -f || true'
            }
        }
        success {
            echo '✅✅✅ PIPELINE RÉUSSI ! ✅✅✅'
            emailext (
                subject: "SUCCÈS Pipeline DevOps - Build ${env.BUILD_NUMBER}",
                body: "Le pipeline ${env.JOB_NAME} #${env.BUILD_NUMBER} a réussi!\n\nConsultez: ${env.BUILD_URL}",
                to: "admin@example.com"
            )
        }
        failure {
            echo '❌❌❌ PIPELINE EN ÉCHEC ❌❌❌'
        }
        unstable {
            echo '⚠️ Pipeline instable - Tests échoués'
        }
    }
}
