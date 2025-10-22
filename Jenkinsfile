pipeline {
    agent any
    
    tools {
        maven 'maven'
    }
    
    environment {
        // Changez le projectKey pour être unique
        PROJECT_KEY = 'DevOps_Project_Jenkins_' + env.BUILD_NUMBER
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '📦 Checkout du code...'
                git branch: 'main', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Build') {
            steps {
                echo '🔨 Compilation...'
                sh 'mvn -B clean compile'
            }
        }
        
        stage('Tests') {
            steps {
                echo '🧪 Exécution des tests...'
                sh 'mvn -B test'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Analyse SonarQube...'
                script {
                    // Méthode directe sans withSonarQubeEnv
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                        mvn -B sonar:sonar \
                          -Dsonar.projectKey=${env.PROJECT_KEY} \
                          -Dsonar.host.url=http://localhost:9001 \
                          -Dsonar.login=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "🏁 Build ${env.BUILD_NUMBER} terminé : ${currentBuild.currentResult}"
        }
    }
}
