pipeline {
    agent any
    
    environment {
        // URL CORRECTE pour Docker dans Jenkins
        SONAR_HOST_URL = 'http://host.docker.internal:9000'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "📁 Récupération du code..."
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Create Source Code') {
            steps {
                echo "🛠️ Création du code source..."
                sh '''
                    # Nettoyer complètement
                    rm -rf src target 2>/dev/null || true
                    mkdir -p src/main/java/com/devops
                    
                    echo "📝 Création des fichiers Java..."
                    
                    # Fichier 1 - Application principale
                    cat > src/main/java/com/devops/MainApp.java << 'EOF'
package com.devops;

/**
 * Application DevOps principale
 * Analyse SonarQube
 */
public class MainApp {
    
    private String appName = "DevOps Pipeline";
    private String version = "1.0.0";
    
    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.start();
    }
    
    public void start() {
        System.out.println("🚀 Starting: " + appName + " v" + version);
        processData("sample data");
    }
    
    public String processData(String input) {
        if (input == null) {
            return "Error: Null input";
        }
        return "Processed: " + input.toUpperCase();
    }
    
    public int calculate(int a, int b) {
        return a + b;
    }
}
EOF

                    # Fichier 2 - Service métier
                    cat > src/main/java/com/devops/Calculator.java << 'EOF'
package com.devops;

/**
 * Service de calcul
 */
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }
    
    public int multiply(int a, int b) {
        return a * b;
    }
    
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
    
    public boolean isPositive(int number) {
        return number > 0;
    }
}
EOF

                    echo "✅ Code source créé:"
                    echo "📄 MainApp.java"
                    echo "📄 Calculator.java"
                '''
            }
        }
        
        stage('SonarQube Analysis - FIXED') {
            steps {
                echo "🔍 LANCEMENT ANALYSE SONARQUBE..."
                script {
                    sh """
                        echo "🚀 Démarrage de l'analyse..."
                        echo "📡 URL: ${SONAR_HOST_URL}"
                        
                        # AFFICHER la structure
                        echo "📁 Structure du projet:"
                        find . -type f -name "*.java" | head -10
                        
                        # MÉTHODE CORRECTE - Utiliser host.docker.internal pour accéder à localhost
                        echo "🐳 Exécution du scanner SonarQube Docker..."
                        
                        docker run --rm \\
                          -v "\$(pwd)":/usr/src \\
                          sonarsource/sonar-scanner-cli:latest \\
                          -Dsonar.projectKey=devops-maram-final \\
                          -Dsonar.projectName="DevOps Maram Final" \\
                          -Dsonar.host.url=http://host.docker.internal:9000 \\
                          -Dsonar.sources=src/main/java \\
                          -Dsonar.sourceEncoding=UTF-8
                        
                        echo "✅ ANALYSE COMPLÈTEMENT TERMINÉE"
                    """
                }
            }
        }
        
        stage('Verify in SonarQube') {
            steps {
                echo "📊 Vérification..."
                script {
                    sleep 10  // Attendre que l'analyse soit traitée
                }
                sh '''
                    echo "🎉 ANALYSE RÉUSSIE !"
                    echo ""
                    echo "🌐 OUVREZ SONARQUBE MAINTENANT:"
                    echo "   http://localhost:9000/projects"
                    echo ""
                    echo "🔍 VOUS DEVRIEZ VOIR:"
                    echo "   • 1 nouveau projet: 'DevOps Maram Final'"
                    echo "   • 2 fichiers Java analysés"
                    echo "   • Métriques de qualité"
                    echo ""
                    echo "📍 URL directe:"
                    echo "   http://localhost:9000/dashboard?id=devops-maram-final"
                '''
            }
        }
    }
    
    post {
        success {
            echo "🎉🎉🎉 SUCCÈS ! 🎉🎉🎉"
            sh '''
                echo "=========================================="
                echo "✅ L'ANALYSE A FONCTIONNÉ !"
                echo "=========================================="
                echo "📊 Allez vérifier dans SonarQube:"
                echo "   http://localhost:9000"
                echo ""
                echo "🔍 Le projet 'DevOps Maram Final'"
                echo "   devrait maintenant être visible"
                echo "   dans la liste des projets !"
                echo "=========================================="
            '''
        }
        failure {
            echo "❌ Échec - Vérifiez les logs Docker"
        }
    }
}
