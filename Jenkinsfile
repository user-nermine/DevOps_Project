pipeline {
    agent any

    tools {
        // Optionnel : spécifier Maven si configuré dans Jenkins
        maven 'M3'
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
                withSonarQubeEnv('sonar-server') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=sample_project'
                }
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
        // Ajoutez ceci pour le statut SonarQube
        always {
            script {
                // Ceci active l'affichage du statut Quality Gate
            }
        }
    }
}
