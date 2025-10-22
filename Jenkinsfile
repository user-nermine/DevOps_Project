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
    stage('Checkout Maram Branch') {
      steps {
        echo '📦 Checkout branche maram...'
        git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
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

    stage('Quality Gate') {
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
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
    always {
      echo '🏁 Fin du pipeline maram'
    }
  }
}
