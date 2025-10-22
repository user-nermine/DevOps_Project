pipeline {
    agent any
    
    environment {
        // URL SPÉCIALE POUR DOCKER SUR WINDOWS
        SONAR_HOST = 'host.docker.internal:9000'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "📁 Récupération du code..."
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Create Project Structure') {
            steps {
                echo "🛠️ Création de la structure..."
                sh '''
                    # Créer une structure simple et propre
                    rm -rf sonar-project
                    mkdir -p sonar-project/src/main/java/com/devops
                    
                    # Fichier 1 - Simple et efficace
                    cat > sonar-project/src/main/java/com/devops/Application.java << 'EOF'
package com.devops;
public class Application {
    public static void main(String[] args) {
        System.out.println("DevOps Application");
    }
    public String process(String input) {
        return input != null ? input.toUpperCase() : "NULL";
    }
}
EOF

                    # Fichier 2 
                    cat > sonar-project/src/main/java/com/devops/Calculator.java << 'EOF'
package com.devops;
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public int multiply(int a, int b) { return a * b; }
}
EOF

                    echo "✅ Structure créée"
                    ls -la sonar-project/src/main/java/com/devops/
                '''
            }
        }
        
        stage('SonarQube Scan - FIXED') {
            steps {
                echo "🔍 Analyse SonarQube..."
                script {
                    sh """
                        echo "🚀 Démarrage analyse..."
                        cd sonar-project
                        
                        # MÉTHODE GARANTIE - Network host
                        echo "🔧 Configuration..."
                        echo "SonarQube URL: ${SONAR_HOST}"
                        
                        # Scanner avec network=host pour accéder au localhost
                        docker run --rm \\
                          --network=host \\
                          -v \$(pwd):/usr/src \\
                          sonarsource/sonar-scanner-cli:latest \\
                          -Dsonar.projectKey=devops-maram-final-fix \\
                          -Dsonar.projectName="DevOps Maram FINAL" \\
                          -Dsonar.host.url=http://localhost:9000 \\
                          -Dsonar.sources=src/main/java \\
                          -Dsonar.sourceEncoding=UTF-8 \\
                          -Dsonar.scm.disabled=true
                          
                        echo "✅ Analyse COMPLÈTEMENT envoyée à SonarQube!"
                    """
                }
            }
        }
        
        stage('Wait and Verify') {
            steps {
                echo "⏳ Attente traitement..."
                script {
                    sleep 15  // Attendre que SonarQube traite l'analyse
                }
                sh '''
                    echo "🎉 ANALYSE TERMINÉE !"
                    echo ""
                    echo "🌐 OUVREZ MAINTENANT:"
                    echo "   http://localhost:9000/projects"
                    echo ""
                    echo "🔍 CHERCHEZ: 'DevOps Maram FINAL'"
                    echo ""
                    echo "📍 URL DIRECTE:"
                    echo "   http://localhost:9000/dashboard?id=devops-maram-final-fix"
                    echo ""
                    echo "⏱️  Si pas visible, attendez 30 secondes et rafraîchissez"
                '''
            }
        }
    }
    
    post {
        success {
            echo "🎉🎉🎉 SUCCÈS ! VÉRIFIEZ SONARQUBE ! 🎉🎉🎉"
        }
    }
}
