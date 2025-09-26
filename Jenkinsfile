pipeline {
    agent any // Utilise un agent Jenkins disponible

    environment {
        PROJECT_NAME = 'projet'
    }

    stages {
        stage('Cloner le code') {
            steps {
                echo "Clonage du dépôt Git..."
                git url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }

        stage('Compiler le projet') {
            steps {
                echo 'Nettoyage et compilation avec Maven...'
                sh 'mvn clean install'
            }
        }

        stage('Tests unitaires') {
            steps {
                echo 'Lancement des tests unitaires...'
                sh 'mvn test'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès !'
        }
        failure {
            echo ' Le pipeline a échoué.'
        }
        always {
            echo  Fin du pipeline (success ou échec).'
        }
    }
}
