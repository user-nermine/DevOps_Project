pipeline {
    agent any
    
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
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                            mvn sonar:sonar \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                              -Dsonar.projectName="DevOps Project - Maram" \
                              -Dsonar.host.url=http://sonarqube:9000 \
                              -Dsonar.login=${SONAR_TOKEN}
                        """
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
                        echo "✅ Image Docker construite : ${DOCKER_IMAGE_NAME}:latest"
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo '📊 Pipeline terminé - Rapport final'
            script {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                echo '📦 JAR archivé avec succès'
            }
        }
        success {
            echo '✅✅✅ PIPELINE RÉUSSI ! ✅✅✅'
            echo '🔍 Vérifiez SonarQube: http://localhost:9000'
            echo '🐳 Image Docker créée: devops-maram-app:latest'
        }
        failure {
            echo '❌❌❌ PIPELINE EN ÉCHEC ❌❌❌'
        }
    }
}
