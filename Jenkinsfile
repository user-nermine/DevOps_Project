pipeline {
    agent any
    
    tools {
        maven 'maven'
    }
    
    environment {
        SONAR_SERVER_NAME = 'SonarQube'
        PROJECT_KEY = 'DevOps_Project'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Build & Tests') {
            steps {
                sh 'mvn -B clean verify'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${env.SONAR_SERVER_NAME}") {
                    sh "mvn -B sonar:sonar -Dsonar.projectKey=${env.PROJECT_KEY}"
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed'
            // Nettoyage si nécessaire
        }
        success {
            echo '✅ Pipeline succeeded!'
        }
        failure {
            echo '❌ Pipeline failed!'
        }
    }
}
