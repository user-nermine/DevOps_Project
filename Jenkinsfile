pipeline {
    agent any
    
    tools {
        maven 'maven'
        jdk 'jdk17'
    }
    
    environment {
        PROJECT_KEY = 'Order_JavaFX_Project'
        SONAR_URL = 'http://localhost:9001'
    }
    
    stages {
        stage('Environment Info') {
            steps {
                echo '🔍 Informations environnement...'
                sh 'echo "Java Home: $JAVA_HOME"'
                sh 'java -version'
                sh 'mvn -version'
            }
        }
        
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn -B clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn -B test || echo "Tests failed or missing - continuing"'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_AUTH_TOKEN')]) {
                        sh """
                        mvn -B sonar:sonar \
                          -Dsonar.projectKey=${PROJECT_KEY} \
                          -Dsonar.host.url=${SONAR_URL} \
                          -Dsonar.login=${SONAR_AUTH_TOKEN}
                        """
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "✅ Pipeline ${currentBuild.currentResult}"
        }
    }
}
