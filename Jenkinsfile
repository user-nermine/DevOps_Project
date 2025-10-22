pipeline {
  agent any

  tools {
    maven 'maven'
  }

  environment {
    SONAR_SERVER_NAME = 'SonarQube'
    SONAR_TOKEN_ID = 'sonar-token'
    PROJECT_KEY = 'DevOps_Project_Maram'
  }

  stages {
    stage('Diagnostic') {
      steps {
        echo '🔍 Diagnostic du workspace...'
        sh '''
          pwd
          ls -la
          find . -name "pom.xml" -type f
          find . -name "*.java" -type f | head -10
          echo "=== Structure complète ==="
          ls -R
        '''
      }
    }

    stage('Build') {
      steps {
        echo '🔨 Compilation et tests...'
        sh 'mvn -B clean verify'
      }
    }

    stage('SonarQube Analysis') {
      steps {
        script {
          withCredentials([string(credentialsId: env.SONAR_TOKEN_ID, variable: 'SONAR_TOKEN')]) {
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
    success {
      echo '✅ Pipeline maram réussi! Vérifiez SonarQube: http://localhost:9001' 
    }
    failure {
      echo '❌ Pipeline maram échoué'
    }
  }
}
