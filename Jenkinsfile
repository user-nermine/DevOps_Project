pipeline {
    agent any
    
    stages {
        stage('Checkout and Analyze') {
            steps {
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
                
                sh '''
                    # Installation de Maven
                    apt-get update
                    apt-get install -y maven
                    
                    # Analyse SonarQube directe
                    mvn clean compile sonar:sonar \
                      -Dsonar.projectKey=DevOps-Project-Maram \
                      -Dsonar.projectName="DevOps Project Maram" \
                      -Dsonar.host.url=http://localhost:9000 \
                      -Dsonar.sources=src/main/java
                    
                    echo "Analyse SonarQube terminee"
                '''
            }
        }
    }
}
