pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://host.docker.internal:9000' // ou http://sonarqube:9000
        SONAR_AUTH_TOKEN = credentials('sonarqube-token') // ID que tu as mis dans Jenkins
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
                sh "mvn sonar:sonar -Dsonar.projectKey=sample_project -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN"
            }
        }
    }

    post {
        success { echo 'Pipeline terminé avec succès ! 🎉' }
        failure { echo 'Le pipeline a échoué ❌' }
    }
}
