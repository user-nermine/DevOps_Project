pipeline {
    agent any

    stages {
        stage('Checkout Git') {
            steps {
                git branch: 'main', url: 'https://github.com/user-nermine/DevOps_Project.git'
                sh 'ls -la'
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                    mvn clean compile test
                    echo "✅ Build et tests terminés"
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=devops-maram \
                          -Dsonar.projectName="DevOps Project Maram" \
                          -Dsonar.sources=src/main/java \
                          -Dsonar.tests=src/test/java \
                          -Dsonar.java.binaries=target/classes \
                          -Dsonar.junit.reportsPath=target/surefire-reports \
                          -Dsonar.sourceEncoding=UTF-8
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts 'target/*.jar'
            }
        }
    }

    post {
        always {
            echo "📊 Pipeline ${currentBuild.result} - Voir SonarQube pour les analyses"
            sh 'find target/ -name "*.jar" | head -5'
        }
        success {
            echo "✅✅✅ SUCCÈS - Analyse SonarQube complète ✅✅✅"
        }
        failure {
            echo "❌ Échec - Vérifiez les logs Jenkins et SonarQube"
        }
    }
}
