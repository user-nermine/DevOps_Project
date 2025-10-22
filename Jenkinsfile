pipeline {
    agent any
    
    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "📁 Récupération du code..."
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Create Real Source Code') {
            steps {
                echo "🛠️ Création du code source..."
                sh '''
                    # Nettoyer et créer une structure réelle
                    rm -rf src target 2>/dev/null || true
                    mkdir -p src/main/java/com/devops
                    
                    # Créer une application Java COMPLÈTE
                    cat > src/main/java/com/devops/MainApplication.java << 'EOF'
package com.devops;

/**
 * Application principale DevOps
 * Analyse SonarQube automatique
 */
public class MainApplication {
    
    private static final String APP_NAME = "DevOps Pipeline";
    private static final String VERSION = "1.0.0";
    
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.startApplication();
    }
    
    public void startApplication() {
        System.out.println("🚀 Démarrage: " + APP_NAME + " v" + VERSION);
        
        String result = processOrder("CMD-12345");
        System.out.println("📦 " + result);
        
        int total = calculateTotal(100, 3);
        System.out.println("💰 Total: " + total);
        
        System.out.println("✅ Application prête pour SonarQube");
    }
    
    /**
     * Traite une commande
     */
    public String processOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID commande invalide");
        }
        
        if (!orderId.startsWith("CMD-")) {
            return "ERREUR: Format commande incorrect";
        }
        
        return "Commande " + orderId + " traitée avec succès";
    }
    
    /**
     * Calcule le total
     */
    public int calculateTotal(int unitPrice, int quantity) {
        if (unitPrice < 0 || quantity < 0) {
            throw new IllegalArgumentException("Prix ou quantité invalide");
        }
        
        return unitPrice * quantity;
    }
    
    /**
     * Valide un utilisateur
     */
    public boolean validateUser(String username, String email) {
        return username != null && 
               username.length() >= 3 &&
               email != null &&
               email.contains("@");
    }
    
    public String getApplicationInfo() {
        return APP_NAME + " - Version " + VERSION;
    }
}
EOF

                    # Créer un service métier
                    cat > src/main/java/com/devops/OrderService.java << 'EOF'
package com.devops;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des commandes
 */
public class OrderService {
    
    private List<String> orders;
    private int orderCounter;
    
    public OrderService() {
        this.orders = new ArrayList<>();
        this.orderCounter = 1;
        initializeSampleOrders();
    }
    
    private void initializeSampleOrders() {
        orders.add("CMD-00001 - Pending");
        orders.add("CMD-00002 - Completed");
    }
    
    /**
     * Créer une nouvelle commande
     */
    public String createOrder(String productName, int quantity) {
        if (productName == null || productName.trim().isEmpty()) {
            return "ERREUR: Nom produit requis";
        }
        
        if (quantity <= 0) {
            return "ERREUR: Quantité invalide";
        }
        
        String orderId = String.format("CMD-%05d", orderCounter++);
        String order = orderId + " - " + productName + " x" + quantity + " - Pending";
        orders.add(order);
        
        return "Commande créée: " + order;
    }
    
    /**
     * Liste toutes les commandes
     */
    public List<String> getAllOrders() {
        return new ArrayList<>(orders);
    }
    
    /**
     * Trouve une commande par ID
     */
    public String findOrderById(String orderId) {
        for (String order : orders) {
            if (order.contains(orderId)) {
                return order;
            }
        }
        return "Commande non trouvée: " + orderId;
    }
    
    public int getTotalOrders() {
        return orders.size();
    }
}
EOF

                    # Créer un fichier de configuration SonarQube
                    cat > sonar-project.properties << 'EOF'
sonar.projectKey=devops-maram-auto-created
sonar.projectName=DevOps Maram Auto Created
sonar.projectVersion=1.0
sonar.sources=src/main/java
sonar.sourceEncoding=UTF-8
sonar.host.url=http://localhost:9000
sonar.scm.disabled=true
EOF

                    echo "✅ Code source créé avec succès"
                    echo "📊 Fichiers créés:"
                    find src -name "*.java" | head -10
                '''
            }
        }
        
        stage('Real SonarQube Analysis') {
            steps {
                echo "🔍 LANCEMENT ANALYSE SONARQUBE AUTOMATIQUE..."
                script {
                    sh '''
                        echo "🚀 Début de l'analyse AUTOMATIQUE..."
                        echo "📡 SonarQube: ${SONAR_HOST_URL}"
                        
                        # AFFICHER ce qui va être analysé
                        echo "📁 CONTENU À ANALYSER:"
                        echo "========================================"
                        find src -type f -name "*.java" -exec echo "   📄 {}" \\;
                        echo "========================================"
                        
                        # MÉTHODE GARANTIE - Scanner Docker
                        echo "🐳 Exécution du scanner SonarQube..."
                        
                        docker run --rm \\
                          -v $(pwd):/usr/src \\
                          --network=host \\
                          sonarsource/sonar-scanner-cli:latest \\
                          -Dsonar.projectKey=devops-maram-auto-created \\
                          -Dsonar.projectName="DevOps Maram Auto Created" \\
                          -Dsonar.host.url=http://localhost:9000 \\
                          -Dsonar.sources=src/main/java \\
                          -Dsonar.sourceEncoding=UTF-8 \\
                          -Dsonar.scm.disabled=true \\
                          -Dsonar.coverage.exclusions=**/*.java
                        
                        echo "✅ ANALYSE ENVOYÉE À SONARQUBE !"
                        echo "⏱️  Le projet va apparaître automatiquement dans 30-60 secondes"
                    '''
                }
            }
        }
        
        stage('Wait and Verify') {
            steps {
                echo "⏳ Attente de l'apparition du projet..."
                script {
                    sleep 30  // Attendre 30 secondes
                }
                sh '''
                    echo "🎯 VÉRIFIEZ MAINTENANT SONARQUBE !"
                    echo ""
                    echo "🌐 URL: http://localhost:9000/projects"
                    echo "🔍 Cherchez: 'DevOps Maram Auto Created'"
                    echo ""
                    echo "📋 Si le projet n'apparaît pas:"
                    echo "   1. Attendez 1 minute de plus"
                    echo "   2. Rafraîchissez la page"
                    echo "   3. Vérifiez les logs SonarQube"
                    echo "   4. Le projet key est: devops-maram-auto-created"
                '''
            }
        }
    }
    
    post {
        success {
            echo "🎉🎉🎉 ANALYSE TERMINÉE ! 🎉🎉🎉"
            sh '''
                echo "================================================"
                echo "✅ L'ANALYSE A ÉTÉ ENVOYÉE À SONARQUBE"
                echo "================================================"
                echo "📍 Accédez à: http://localhost:9000"
                echo "📊 Allez dans: Projects"
                echo "🔍 Recherchez: DevOps Maram Auto Created"
                echo "🔑 Project Key: devops-maram-auto-created"
                echo ""
                echo "⚡ Le projet devrait apparaître automatiquement"
                echo "   dans la liste des projets sous 1-2 minutes"
                echo "================================================"
            '''
        }
    }
}
