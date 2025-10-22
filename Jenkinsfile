pipeline {
    agent any

    stages {
        stage('Ultra Simple Test') {
            steps {
                sh '''
                    # Créer un seul fichier
                    mkdir -p simple-test/src
                    echo "public class Test { public void run() {} }" > simple-test/src/Test.java

                    # Analyse ultra simple
                    docker run --rm \\
                      -v $(pwd)/simple-test:/usr/src \\
                      sonarsource/sonar-scanner-cli:latest \\
                      -Dsonar.projectKey=simple-test-$(date +%s) \\
                      -Dsonar.projectName="Simple Test" \\
                      -Dsonar.host.url=http://host.docker.internal:9000 \\
                      -Dsonar.sources=src

                    echo "✅ Done! Check SonarQube: http://localhost:9000/projects"
                '''
            }
        }
    }
}
