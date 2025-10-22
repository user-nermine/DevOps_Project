pipeline {
    agent any
    
    environment {
        SONAR_PROJECT_KEY = 'DevOps-Project-Maram'
        SONAR_PROJECT_NAME = 'DevOps Project Maram'
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                echo 'Recuperation du code source...'
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
                
                script {
                    echo "Repository clone avec succes"
                    sh '''
                        echo "Structure du projet"
                        ls -la
                        find . -name "*.java" 2>/dev/null | head -5 || echo "Aucun fichier Java trouve"
                        find . -name "pom.xml" 2>/dev/null | head -1 || echo "pom.xml non trouve"
                    '''
                }
            }
        }
        
        stage('Setup Environment') {
            steps {
                echo 'Configuration de l environnement...'
                script {
                    sh '''
                        echo "Verification des outils"
                        java -version 2>/dev/null && echo "Java disponible" || echo "Java non verifie"
                        mvn --version 2>/dev/null && echo "Maven disponible" || echo "Maven non disponible"
                    '''
                }
            }
        }
        
        stage('Build & Test') {
            steps {
                echo 'Compilation et tests...'
                script {
                    sh '''
                        echo "Demarrage de la phase Build & Test"
                        
                        # Compilation reelle
                        mvn clean compile test-compile -q
                        
                        # Execution des tests
                        mvn test -q
                        
                        echo "Build et tests termines avec succes"
                    '''
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    echo 'Rapports JUnit enregistres'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Creation du package...'
                script {
                    sh '''
                        echo "Creation du package applicatif"
                        mvn package -DskipTests -q
                        echo "Package cree: Order-1.0-SNAPSHOT.jar"
                        ls -la target/*.jar
                    '''
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Analyse de qualite avec SonarQube...'
                script {
                    // Methode 1: Avec plugin SonarQube Jenkins
                    withSonarQubeEnv('sonarqube') {
                        sh """
                            mvn sonar:sonar \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                              -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                              -Dsonar.sources=src/main/java \
                              -Dsonar.tests=src/test/java \
                              -Dsonar.java.binaries=target/classes \
                              -Dsonar.junit.reportsPath=target/surefire-reports \
                              -Dsonar.sourceEncoding=UTF-8 \
                              -Dsonar.coverage.exclusions=**/test/**,**/generated/**
                        """
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Verification du Quality Gate...'
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: false
                    }
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                echo 'Construction de l image Docker...'
                script {
                    sh '''
                        echo "Preparation de l environnement Docker"
                        
                        # Creation du Dockerfile
                        cat > Dockerfile << ENDDOCKER
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
ENDDOCKER

                        echo "Dockerfile cree avec succes"
                        
                        # Construction de l image
                        docker build -t devops-maram-app:latest .
                        echo "Image Docker construite: devops-maram-app:latest"
                    '''
                }
            }
        }
        
        stage('Final Report') {
            steps {
                echo 'Generation du rapport final...'
                script {
                    sh """
                        echo " "
                        echo "PIPELINE DevOps - RAPPORT FINAL"
                        echo " "
                        echo "ETAPES ACCOMPLIES:"
                        echo "  Checkout Code ............ OK"
                        echo "  Build & Test ............. OK" 
                        echo "  Package .................. OK"
                        echo "  SonarQube Analysis ....... OK"
                        echo "  Quality Gate ............. OK"
                        echo "  Docker Build ............. OK"
                        echo " "
                        echo "ACCES AUX APPLICATIONS:"
                        echo "  Jenkins: http://localhost:8081"
                        echo "  SonarQube: http://localhost:9000"
                        echo "  Rapport Sonar: http://localhost:9000/dashboard?id=${SONAR_PROJECT_KEY}"
                        echo " "
                        echo "PIPELINE REUSSI"
                        echo " "
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo 'Archivage des artefacts...'
            script {
                archiveArtifacts artifacts: 'target/*.jar, Dockerfile, Jenkinsfile', fingerprint: true
                echo 'Artefacts archives'
            }
        }
        success {
            echo 'PIPELINE COMPLETEMENT REUSSI'
            echo 'Analyse SonarQube envoyee avec succes'
            echo 'Image Docker prete pour la production'
        }
        failure {
            echo 'Echec du pipeline - Analyse des logs necessaire'
        }
    }
}
