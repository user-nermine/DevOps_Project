pipeline {
    agent any

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
                    rm -rf jenkins-auto-project
                    mkdir -p jenkins-auto-project/src/main/java/com/devops

                    # Créer plusieurs fichiers Java COMPLETS
                    cat > jenkins-auto-project/src/main/java/com/devops/MainApplication.java << 'EOF'
package com.devops;

/**
 * Application principale créée automatiquement par Jenkins
 */
public class MainApplication {
    
    private String appName = "Jenkins Auto Project";
    private String version = "1.0.0";
    
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.start();
    }
    
    public void start() {
        System.out.println("🚀 Démarrage: " + appName + " v" + version);
        
        OrderService orderService = new OrderService();
        String orderResult = orderService.processOrder("ORDER-123");
        System.out.println("📦 " + orderResult);
        
        UserService userService = new UserService();
        boolean isValid = userService.validateUser("john_doe");
        System.out.println("👤 Utilisateur valide: " + isValid);
        
        System.out.println("✅ Application prête pour SonarQube");
    }
    
    public String getAppInfo() {
        return appName + " - Version: " + version;
    }
}
EOF

                    cat > jenkins-auto-project/src/main/java/com/devops/OrderService.java << 'EOF'
package com.devops;

/**
 * Service de gestion des commandes
 */
public class OrderService {
    
    public String processOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return "❌ ERREUR: ID commande manquant";
        }
        
        if (!orderId.startsWith("ORDER-")) {
            return "❌ ERREUR: Format commande invalide";
        }
        
        return "✅ Commande " + orderId + " traitée avec succès";
    }
    
    public double calculateTotal(double price, int quantity) {
        if (price < 0 || quantity < 0) {
            throw new IllegalArgumentException("Prix ou quantité invalide");
        }
        return price * quantity;
    }
}
EOF

                    cat > jenkins-auto-project/src/main/java/com/devops/UserService.java << 'EOF'
package com.devops;

/**
 * Service de gestion des utilisateurs
 */
public class UserService {
    
    public boolean validateUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        if (username.length() < 3) {
            return false;
        }
        
        // Vérifier que le username ne contient pas de caractères spéciaux
        return username.matches("^[a-zA-Z0-9_]+$");
    }
    
    public String createUser(String username, String email) {
        if (!validateUser(username)) {
            return "❌ Nom d'utilisateur invalide";
        }
        
        if (email == null || !email.contains("@")) {
            return "❌ Email invalide";
        }
        
        return "✅ Utilisateur " + username + " créé avec succès";
    }
}
EOF

                    echo "✅ Code source créé avec succès"
                    echo "📊 Fichiers générés:"
                    find jenkins-auto-project -name "*.java" | head -10
                '''
            }
        }

        stage('Network Test') {
            steps {
                echo "🔍 Test de connexion SonarQube..."
                sh '''
                    echo "Test 1: Depuis Jenkins..."
                    curl -s http://localhost:9000/api/system/status && echo "✅ Jenkins → SonarQube: OK" || echo "❌ Jenkins → SonarQube: FAIL"
                    
                    echo "Test 2: Depuis Docker..."
                    docker run --rm curlimages/curl:latest curl -s http://host.docker.internal:9000/api/system/status && echo "✅ Docker → SonarQube: OK" || echo "❌ Docker → SonarQube: FAIL"
                '''
            }
        }

        stage('SonarQube Auto Analysis') {
            steps {
                echo "🔍 LANCEMENT ANALYSE AUTOMATIQUE..."
                sh '''
                    cd jenkins-auto-project
                    echo "🚀 Création et analyse AUTOMATIQUE du projet..."
                    
                    # CRÉATION AUTOMATIQUE via analyse - PAS de création manuelle
                    docker run --rm \\
                      -v $(pwd):/usr/src \\
                      sonarsource/sonar-scanner-cli:latest \\
                      -Dsonar.projectKey=jenkins-auto-project-${BUILD_NUMBER} \\
                      -Dsonar.projectName="Jenkins Auto Project ${BUILD_NUMBER}" \\
                      -Dsonar.host.url=http://host.docker.internal:9000 \\
                      -Dsonar.sources=src/main/java \\
                      -Dsonar.sourceEncoding=UTF-8 \\
                      -Dsonar.scm.disabled=true
                    
                    echo "✅ ANALYSE COMPLÈTEMENT ENVOYÉE À SONARQUBE"
                '''
            }
        }

        stage('Wait for Processing') {
            steps {
                echo "⏳ Attente du traitement SonarQube..."
                script {
                    sleep 30  // Attendre que SonarQube traite l'analyse
                }
            }
        }

        stage('Verify in SonarQube') {
            steps {
                echo "📊 Vérification dans SonarQube..."
                sh '''
                    echo "🎉 ANALYSE TERMINÉE !"
                    echo ""
                    echo "🌐 OUVREZ SONARQUBE MAINTENANT:"
                    echo "   http://localhost:9000/projects"
                    echo ""
                    echo "🔍 VOUS DEVRIEZ VOIR:"
                    echo "   • 'Jenkins Auto Project ${BUILD_NUMBER}'"
                    echo "   • 3 fichiers Java analysés"
                    echo "   • Métriques de qualité"
                    echo ""
                    echo "📍 URL DIRECTE:"
                    echo "   http://localhost:9000/dashboard?id=jenkins-auto-project-${BUILD_NUMBER}"
                    echo ""
                    echo "📋 SI LE PROJET N'APPARAÎT PAS:"
                    echo "   1. Attendez 1 minute"
                    echo "   2. Rafraîchissez la page"
                    echo "   3. Vérifiez les logs SonarQube"
                '''
            }
        }
    }

    post {
        success {
            echo "🎉🎉🎉 SUCCÈS ! PROJET CRÉÉ AUTOMATIQUEMENT 🎉🎉🎉"
            sh '''
                echo "================================================"
                echo "✅ JENKINS A CRÉÉ ET ANALYSÉ LE PROJET AUTOMATIQUEMENT"
                echo "================================================"
                echo "📊 Allez vérifier dans SonarQube:"
                echo "   http://localhost:9000/projects"
                echo ""
                echo "🔍 Le projet 'Jenkins Auto Project ${BUILD_NUMBER}'"
                echo "   devrait être visible dans la liste"
                echo ""
                echo "⚡ Aucune création manuelle nécessaire !"
                echo "================================================"
            '''
        }
        failure {
            echo "❌ Échec - Vérifiez les logs Jenkins"
        }
    }
}
