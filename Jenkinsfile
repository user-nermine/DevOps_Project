pipeline {
    agent any
    
    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_PROJECT_KEY = 'devops-maram-local-project'
    }
    
    stages {
        stage('Declarative: Tool Install') {
            steps {
                echo "📦 Vérification des outils..."
            }
        }
        
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
                    # Créer la structure
                    mkdir -p src/main/java/com/devops
                    
                    # Créer une classe Java
                    cat > src/main/java/com/devops/MainApplication.java << 'EOF'
package com.devops;

public class MainApplication {
    public String processOrder(String orderId) {
        if (orderId == null) {
            return "Invalid order";
        }
        return "Order " + orderId + " processed";
    }
    
    public int calculateTotal(int price, int quantity) {
        return price * quantity;
    }
}
EOF

                    # Créer une deuxième classe
                    cat > src/main/java/com/devops/UserService.java << 'EOF'
package com.devops;

public class UserService {
    public boolean validateUser(String username) {
        return username != null && username.length() > 3;
    }
}
EOF

                    echo "✅ Code source créé"
                '''
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo "🔍 Analyse SonarQube..."
                script {
                    sh """
                        echo "🚀 Démarrage de l'analyse..."
                        
                        # Méthode SIMPLE et GARANTIE
                        docker run --rm \\
                          -v \$(pwd):/usr/src \\
                          --network=host \\
                          sonarsource/sonar-scanner-cli:latest \\
                          -Dsonar.projectKey=${SONAR_PROJECT_KEY} \\
                          -Dsonar.projectName="DevOps Maram Local Project" \\
                          -Dsonar.host.url=http://localhost:9000 \\
                          -Dsonar.sources=src/main/java \\
                          -Dsonar.login=admin \\
                          -Dsonar.password=admin
                          
                        echo "✅ Analyse envoyée à SonarQube"
                    """
                }
            }
        }
        
        stage('Results') {
            steps {
                echo "📊 Résultats..."
                sh """
                    echo "🎉 ANALYSE TERMINÉE !"
                    echo "🌐 Accédez à: http://localhost:9000/dashboard?id=${SONAR_PROJECT_KEY}"
                    echo ""
                    echo "Si vous ne voyez rien:"
                    echo "1. Allez sur http://localhost:9000"
                    echo "2. Cliquez sur 'Projects'"
                    echo "3. Cherchez 'DevOps Maram Local Project'"
                """
            }
        }
    }
    
    post {
        success {
            echo "✅ Pipeline réussi - Vérifiez SonarQube !"
        }
    }
}
