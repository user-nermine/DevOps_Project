pipeline {
    agent any

    tools {
        maven 'M3'  // Assurez-vous que Maven est configuré dans "Global Tool Configuration"
    }

    environment {
        // Ces variables ne sont plus nécessaires avec withSonarQubeEnv
        // SONAR_HOST_URL = 'http://host.docker.internal:9001'
        // SONAR_AUTH_TOKEN = credentials('sonarqube-token')
    }

    stages {
        stage('Cloner le code') {
            steps {
                git url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }

        stage('Compiler le projet') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Tests unitaires') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Analyse SonarQube') {
            steps {
                withSonarQubeEnv('sonar-server') {  // ← MUST MATCH Jenkins config
                    sh 'mvn sonar:sonar -Dsonar.projectKey=sample_project'
                }
            }
        }
    }

    post {
        always {
            // Ceci active l'affichage du statut Quality Gate
            script {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    error "Quality Gate failed: ${qg.status}"
                }
            }
        }
    }
}
