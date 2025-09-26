pipeline {
    agent any  // Utilise un agent Jenkins disponible

    environment {
        
        PROJECT_NAME = 'projet'
    }

    stages {
        stage('Cloner le code') {
            steps {
                git url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }

        stage('Compiler le projet') {
            steps {
                echo 'Compilation avec Maven...'
                sh 'mvn clean'
            }
        }

        stage('Tests unitaires') {
            steps {
                echo 'Lancement des tests...'
                sh 'mvn test'
            }
        }

       

        
    }

    post {
        success {
            echo 'Pipeline terminé avec succès ! '
        }
        failure {
            echo 'Le pipeline a échoué '
        }
        always {
            echo 'Fin du pipeline (success ou échec)'
        }
    }
}
