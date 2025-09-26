
pipeline {
    agent any  // Utilise un agent Jenkins disponible

    environment {
        // Variables d'environnement si besoin
        PROJECT_NAME = 'project'
    }

    stages {
        stage('Cloner le code') {
            steps {
                git url: 'https://github.com/votre-utilisateur/votre-repo.git'
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

        stage('Analyse statique (optionnel)') {
            steps {
                echo 'Analyse statique (ex: Checkstyle, PMD, SonarQube)'
                // Exemple : sh 'mvn checkstyle:check'
            }
        }

        
    }

    post {
        success {
            echo 'Pipeline terminé avec succès ! 🎉'
        }
        failure {
            echo 'Le pipeline a échoué ❌'
        }
        always {
            echo 'Fin du pipeline (success ou échec)'
        }
    }
}

