pipeline {
    agent any
    tools {
        maven 'maven'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "📁 Récupération du code..."
                checkout scm
            }
        }
        
        stage('Build & Test') {
            steps {
                echo "🔨 Compilation et tests..."
                sh 'mvn -B clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo "📦 Création du package JAR..."
                sh 'mvn -B package -DskipTests'
            }
            post {
                success {
                    script {
                        if (fileExists('target/Order-1.0-SNAPSHOT.jar')) {
                            echo "✅ JAR généré: Order-1.0-SNAPSHOT.jar"
                            sh 'ls -lh target/Order-1.0-SNAPSHOT.jar'
                        }
                    }
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo "🔍 Analyse SonarQube..."
                script {
                    try {
                        withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                            sh """
                                mvn sonar:sonar \
                                  -Dsonar.projectKey=order-project \
                                  -Dsonar.host.url=http://sonarqube:9000 \
                                  -Dsonar.login=${SONAR_TOKEN}
                            """
                        }
                    } catch (Exception e) {
                        echo "⚠️ SonarQube ignoré - Configurez SONAR_TOKEN dans Jenkins"
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "📊 Pipeline terminé"
            script {
                if (fileExists('target/Order-1.0-SNAPSHOT.jar')) {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    echo "📦 Artifact archivé"
                }
            }
        }
        success {
            echo "✅✅✅ SUCCÈS ✅✅✅"
        }
        failure {
            echo "❌❌❌ ÉCHEC ❌❌❌"
        }
    }
}
