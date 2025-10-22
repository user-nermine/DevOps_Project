pipeline {
    agent any
    tools {
        maven 'maven'  // Correspond à votre installation Maven configurée dans Jenkins
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
                // Éventuellement cloner le repository si nécessaire
                // git 'url-du-repo'
            }
        }
        
        stage('Build & Test') {
            steps {
                echo "🔨 Compilation et tests..."
                sh 'mvn -B clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'  // Archive des résultats de tests
                    // Vérification que la compilation a produit les artifacts
                    script {
                        if (fileExists('target')) {
                            sh 'ls -la target/ || echo "Dossier target vide ou inexistant"'
                        } else {
                            echo "⚠️ Le dossier target n'existe pas après la compilation"
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
                        def jarFiles = findFiles(glob: 'target/*.jar')
                        if (jarFiles.length > 0) {
                            echo "✅ Fichiers JAR générés: ${jarFiles*.name}"
                        } else {
                            error "❌ Aucun fichier JAR trouvé dans target/"
                        }
                    }
                }
            }
        }
        
        stage('SonarQube Analysis') {
            environment {
                SONAR_HOST_URL = 'https://votre-sonar-server'
            }
            steps {
                echo "🔍 Analyse SonarQube..."
                withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN_SECURE')]) {
                    sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=votre-project-key \
                          -Dsonar.host.url=${SONAR_HOST_URL} \
                          -Dsonar.login=${SONAR_TOKEN_SECURE}
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo "📊 Pipeline terminé - Consultez les rapports ci-dessus"
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        success {
            echo "✅ Pipeline exécuté avec succès!"
        }
        failure {
            echo "❌ Échec du pipeline - Vérifiez les logs"
        }
    }
}
