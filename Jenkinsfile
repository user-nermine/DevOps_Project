pipeline {
    agent any
    tools {
        maven 'maven'
    }
    
    stages {
        stage('Tool Install') {
            steps {
                echo "🔧 Installation et configuration des outils..."
                sh 'mvn --version'
            }
        }
        
        stage('Create Complete Project') {
            steps {
                echo "📁 Préparation du projet..."
            }
        }
        
        stage('Build & Test') {
            steps {
                echo "🔨 Compilation et tests..."
                sh 'mvn -B clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    script {
                        if (fileExists('target')) {
                            sh 'ls -la target/'
                        } else {
                            echo "⚠️ Le dossier target n'existe pas"
                        }
                    }
                }
            }
        }
        
        stage('Package') {
            steps {
                echo "📦 Création du package JAR..."
                sh 'mvn -B package -DskipTests'
            }
            post {
                success {
                    script {
                        // Méthode alternative à findFiles
                        def jarExists = fileExists('target/Order-1.0-SNAPSHOT.jar')
                        if (jarExists) {
                            echo "✅ Fichier JAR généré: Order-1.0-SNAPSHOT.jar"
                            sh 'ls -la target/*.jar'
                        } else {
                            // Vérification d'autres noms possibles
                            sh '''
                                echo "🔍 Recherche des fichiers JAR..."
                                ls -la target/ | grep ".jar" || echo "Aucun fichier JAR trouvé"
                            '''
                            error "❌ Aucun fichier JAR trouvé dans target/"
                        }
                    }
                }
            }
        }
        
        stage('SonarQube Analysis') {
            environment {
                SONAR_HOST_URL = 'https://votre-sonar-server'  // À adapter
            }
            steps {
                echo "🔍 Analyse SonarQube..."
                withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN_SECURE')]) {
                    sh """
                        mvn sonar:sonar \\
                          -Dsonar.projectKey=order-project \\
                          -Dsonar.host.url=${SONAR_HOST_URL} \\
                          -Dsonar.login=${SONAR_TOKEN_SECURE}
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo "📊 Pipeline terminé - Consultez les rapports ci-dessus"
            // Archive uniquement si le JAR existe
            script {
                if (fileExists('target/Order-1.0-SNAPSHOT.jar')) {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    echo "📦 Artifacts archivés avec succès"
                } else {
                    echo "⚠️ Aucun artifact à archiver"
                }
            }
        }
        success {
            echo "✅ Pipeline exécuté avec succès!"
        }
        failure {
            echo "❌ Échec du pipeline - Vérifiez les logs"
        }
    }
}
