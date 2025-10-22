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
                sh '''
                    echo "Structure du projet :"
                    ls -la
                    echo "Fichiers sources :"
                    find . -name "*.java" -type f | head -10 || echo "Aucun fichier Java trouvé"
                '''
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
                            echo "✅ Dossier target créé avec succès"
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
                        if (fileExists('target/Order-1.0-SNAPSHOT.jar')) {
                            echo "✅ Fichier JAR généré : Order-1.0-SNAPSHOT.jar"
                            sh 'ls -lh target/Order-1.0-SNAPSHOT.jar'
                        } else {
                            echo "❌ Aucun fichier JAR trouvé"
                            sh 'ls -la target/ || echo "Dossier target inaccessible"'
                        }
                    }
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo "🔍 Analyse SonarQube..."
                script {
                    try {
                        withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN_SECURE')]) {
                            sh """
                                mvn sonar:sonar \
                                  -Dsonar.projectKey=order-project \
                                  -Dsonar.projectName="Order Service" \
                                  -Dsonar.host.url=http://localhost:9000 \
                                  -Dsonar.login=${SONAR_TOKEN_SECURE}
                            """
                        }
                    } catch (Exception e) {
                        echo "⚠️ SonarQube ignoré : Token non configuré"
                        echo "ℹ️ Configurez le credential 'SONAR_TOKEN' dans Jenkins"
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "📊 Pipeline terminé - Consultez les rapports"
            script {
                if (fileExists('target/Order-1.0-SNAPSHOT.jar')) {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    echo "📦 Artifacts archivés"
                }
            }
        }
        success {
            echo "✅✅✅ PIPELINE RÉUSSI ! ✅✅✅"
        }
        failure {
            echo "❌❌❌ PIPELINE EN ÉCHEC ❌❌❌"
        }
    }
}
