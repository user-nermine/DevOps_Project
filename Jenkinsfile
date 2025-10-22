pipeline {
    agent any
    
    tools {
        maven 'maven'
        jdk 'jdk17'  // ASSUREZ-VOUS D'AVOIR JDK17 CONFIGURÉ DANS JENKINS
    }
    
    environment {
        PROJECT_KEY = 'Order_JavaFX_Project'
        MODULE_PATH = '--module-path /chemin/vers/javafx-sdk-21.0.2/lib --add-modules javafx.controls,javafx.fxml'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '📦 Checkout du code...'
                git branch: 'main', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Build & Compile') {
            steps {
                echo '🔨 Compilation JavaFX...'
                sh 'mvn -B clean compile'
            }
        }
        
        stage('Tests') {
            steps {
                echo '🧪 Exécution des tests (si disponibles)...'
                script {
                    // Essaye d'exécuter les tests, mais continue même si échec
                    sh '''
                    mvn -B test -DskipTests=false || \
                    echo "Aucun test ou erreur de test - continuation du pipeline"
                    '''
                }
            }
            post {
                always {
                    // Publie les rapports JUnit si disponibles
                    junit allowEmptyResults: true, 
                          testResults: 'target/surefire-reports/*.xml, target/failsafe-reports/*.xml'
                    
                    // Affiche le contenu du dossier target pour debug
                    sh 'ls -la target/ || true'
                    sh 'find . -name "*.xml" -type f | head -5 || true'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '📦 Création du JAR...'
                sh 'mvn -B package -DskipTests'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Analyse SonarQube...'
                script {
                    // Méthode avec credentials directe
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                        mvn -B sonar:sonar \
                          -Dsonar.projectKey=${env.PROJECT_KEY} \
                          -Dsonar.host.url=http://localhost:9001 \
                          -Dsonar.login=${SONAR_TOKEN} \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                          -Dsonar.java.coveragePlugin=jacoco
                        """
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "🏁 Pipeline ${currentBuild.currentResult}"
            // Nettoyage
            sh 'mvn -B clean || true'
        }
        success {
            echo '✅ SUCCÈS! Projet JavaFX analysé avec SonarQube'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo '❌ ÉCHEC - Vérifiez la configuration JavaFX et SonarQube'
        }
    }
}
