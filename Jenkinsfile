pipeline {
    agent any

    environment {
        IMAGE_NAME = 'mon-image'
        IMAGE_TAG = 'latest'

        PROJECT_NAME = 'demo-project'
        SONAR_HOST_URL = 'http://192.168.33.10:9000/'
        SONAR_AUTH_TOKEN = credentials('sonarqube') // token stored in Jenkins credentials
    }

    tools {
        maven 'maven'
    }

    stages {
       
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
                dir('Order'){
                sh 'mvn clean compile'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Build déclenché automatiquement'
                 dir('Order'){
                sh 'mvn clean verify'
                 }
            }
        }

        stage('Tests unitaires') {
            steps {
                echo 'Lancement des tests...'
                 dir('Order'){
                sh 'mvn test'
                 }  
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
                 dir('Order'){
                sh """
                    mvn sonar:sonar \
                        -Dsonar.projectKey=sample_project \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_AUTH_TOKEN}
                """
                 }
            }
        }

          stage('Build Docker Image') {
            steps {
                dir('Order') {
                    script {
                        def image = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                    }
                }
            }
          }

                stage('List Docker Images') {
            steps {
                sh 'docker images'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès !'
        }
        failure {
            echo '  Le pipeline a échoué.'
        }
        always {
            echo '🏁 Fin du pipeline (success ou échec).'
        }
    }
}


















