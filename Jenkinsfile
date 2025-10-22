pipeline {
    agent any

    environment {
        PROJECT_NAME = 'jenkins'
        SONARQUBE_ENV = 'SonarQube'   // Nom du serveur SonarQube configuré dans Jenkins (Manage Jenkins > Configure System)
    }

    stages {
        stage('Cloner le code') {
            steps {
                echo "📦 Clonage de la branche 'Hadil'..."
                git(
                    branch: 'Hadil',
                    url: 'https://github.com/user-nermine/DevOps_Project.git'
                )
            }
        }

        stage('Compiler le projet') {
            steps {
                echo '⚙️ Compilation avec Maven...'
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Tests unitaires') {
            steps {
                echo '🧪 Lancement des tests unitaires...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml' // publication des résultats de tests
                }
            }
        }

     mvn clean verify sonar:sonar \
  -Dsonar.projectKey=jenkins \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=squ_8c1d51ba63f07c04cc656083f903d42262fff429
        stage('Vérification de la qualité du code') {
            steps {
                echo '✅ Vérification du Quality Gate SonarQube...'
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        success {
            echo '🎉 Pipeline terminé avec succès.'
        }
        failure {
            echo '❌ Le pipeline a échoué.'
        }
        always {
            echo '🏁 Fin du pipeline (réussite ou échec).'
        }
    }
}
