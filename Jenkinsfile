pipeline {
    agent any

    environment {
        PROJECT_NAME = 'projet'
    }

    stages {
        stage('Cloner le code') {
            steps {
                echo "Clonage de la branche 'manel'..."
                git(
                    branch: 'manel',
                    url: 'https://github.com/user-nermine/DevOps_Project.git'
                )
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
            echo 'Pipeline termine avec succes.'
        }
        failure {
            echo 'Le pipeline a echoue.'
        }
        always {
            echo 'Fin du pipeline (reussite ou echec).'
        }
    }
}
