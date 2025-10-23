pipeline {
    agent any
    tools {
        maven "M2_HOME"
        jdk "JAVA_HOME"
    }

    environment{
        DOCKER_USERNAME = "hadil04"
        DOCKER_PASSWORD = "97442570"
    }
    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling code...'
                git branch: 'Hadil', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
          stage('Compiling the artifact') {             
            steps {
                echo "compiling"
                sh 'mvn compile'
            }
        }
        stage('mvn SonarQube') {
            steps {
                script {
                       withCredentials ([string(credentialsId: 'token-sonar', variable: 'sonar')]){
                           sh "mvn sonar:sonar -Dsonar.login=${sonar}"}
                
               
                }
            }
        }

        
             

        stage('Build image') {             
            steps {
                sh "docker build -t image ."
            }
        }
        
        stage('Push Docker Image') {
           steps {
              withCredentials([usernamePassword(credentialsId: 'docker-token', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
                sh "docker tag image ${DOCKER_USERNAME}/image:latest"
                sh "docker push ${DOCKER_USERNAME}/image:latest"
              }
           }
        }



    }
}
