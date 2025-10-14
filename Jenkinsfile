pipeline {
    agent any

    environment {
        PROJECT_NAME = 'demo-project'
        SONAR_HOST_URL = 'http://192.168.33.10:9000/'
        SONAR_AUTH_TOKEN = credentials('sonarqube') // token stored in Jenkins credentials
    }

    tools {
        maven 'maven'
    }

    stages {
        stage('Cloner le code') {
            steps {
                git url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
         stage('git') {
            steps {
                checkout scmGit(branches: [[name: '*/Nermine']],
                extensions: [],
                userRemoteConfigs: [[credentialsId: 'rabbi_ysahel',
                url: 'https://github.com/user-nermine/DevOps_Project.git']])
            }
        }

        stage('Compiler le projet') {
            steps {
                echo 'Compilation avec Maven...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Build déclenché automatiquement'
                sh 'mvn clean verify'
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
                echo 'Analyse statique (ex: Checkstyle, PMD, etc.)'
                // sh 'mvn checkstyle:check'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Analyse SonarQube en cours...'
                sh """
                    mvn sonar:sonar \
                        -Dsonar.projectKey=sample_project \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_AUTH_TOKEN}
                """
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès !'
        }
        failure {
            echo '    Le pipeline a échoué.'
        }
        always {
            echo '🏁 Fin du pipeline (success ou échec).'
        }
    }
}











