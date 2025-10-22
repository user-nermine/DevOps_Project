pipeline {
    agent any
    
    stages {
        stage('Diagnostic') {
            steps {
                echo "🔍 DIAGNOSTIC SONARQUBE..."
                sh '''
                    echo "=== DIAGNOSTIC DÉBUT ==="
                    echo "1. Vérification SonarQube..."
                    curl -f http://localhost:9000/api/system/status && echo "✅ SonarQube ACCESSIBLE" || echo "❌ SonarQube INACCESSIBLE"
                    
                    echo "2. Vérification Docker..."
                    docker --version && echo "✅ Docker DISPONIBLE" || echo "❌ Docker INDISPONIBLE"
                    
                    echo "3. Vérification fichiers source..."
                    find . -name "*.java" 2>/dev/null | head -5 || echo "❌ Aucun fichier Java trouvé"
                    
                    echo "4. Test connexion réseau..."
                    docker run --rm alpine ping -c 2 host.docker.internal && echo "✅ Réseau Docker OK" || echo "❌ Problème réseau Docker"
                    echo "=== DIAGNOSTIC FIN ==="
                '''
            }
        }
        
        stage('Create Real Project') {
            steps {
                echo "🛠️ Création projet réel..."
                sh '''
                    # Nettoyer et créer une structure COMPLÈTE
                    rm -rf my-sonar-project
                    mkdir -p my-sonar-project/src/main/java/com/devops
                    cd my-sonar-project
                    
                    # Créer un pom.xml pour Maven
                    cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.devops</groupId>
    <artifactId>sonar-test-project</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <name>SonarQube Test Project</name>
    
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <sonar.projectKey>devops-maram-real-project</sonar.projectKey>
        <sonar.projectName>DevOps Maram Real Project</sonar.projectName>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
EOF

                    # Créer plusieurs fichiers Java
                    cat > src/main/java/com/devops/MainApplication.java << 'EOF'
package com.devops;

/**
 * Application principale pour SonarQube
 */
public class MainApplication {
    public static void main(String[] args) {
        System.out.println("Hello SonarQube!");
        Calculator calc = new Calculator();
        System.out.println("5 + 3 = " + calc.add(5, 3));
    }
}
EOF

                    cat > src/main/java/com/devops/Calculator.java << 'EOF'
package com.devops;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public int multiply(int a, int b) {
        return a * b;
    }
}
EOF

                    cat > src/main/java/com/devops/UserService.java << 'EOF'
package com.devops;

public class UserService {
    public boolean validateUser(String username) {
        return username != null && username.length() > 3;
    }
}
EOF

                    echo "✅ Projet créé dans: my-sonar-project/"
                    ls -la src/main/java/com/devops/
                '''
            }
        }
        
        stage('Method 1: Docker Scanner') {
            steps {
                echo "🔍 MÉTHODE 1: Scanner Docker..."
                sh '''
                    cd my-sonar-project
                    echo "🚀 Lancement analyse avec Docker..."
                    
                    docker run --rm \\
                      -v $(pwd):/usr/src \\
                      -e SONAR_HOST_URL="http://host.docker.internal:9000" \\
                      sonarsource/sonar-scanner-cli:latest \\
                      -Dsonar.projectKey=devops-maram-docker-method \\
                      -Dsonar.projectName="DevOps Maram Docker Method" \\
                      -Dsonar.host.url=http://host.docker.internal:9000 \\
                      -Dsonar.sources=src/main/java \\
                      -Dsonar.sourceEncoding=UTF-8
                    
                    echo "✅ Méthode Docker terminée"
                '''
            }
        }
        
        stage('Method 2: Manual API Call') {
            steps {
                echo "🔍 MÉTHODE 2: Appel API direct..."
                sh '''
                    cd my-sonar-project
                    echo "📡 Test API SonarQube..."
                    
                    # Créer un token manuellement (remplacez ADMIN_PASSWORD)
                    curl -u admin:admin -X POST "http://localhost:9000/api/user_tokens/generate" \
                      -d "name=jenkins-token" \
                      -d "type=GLOBAL_ANALYSIS" || echo "❌ Erreur création token"
                      
                    echo "✅ Test API effectué"
                '''
            }
        }
        
        stage('Method 3: Direct Analysis') {
            steps {
                echo "🔍 MÉTHODE 3: Analyse directe..."
                sh '''
                    cd my-sonar-project
                    echo "🎯 Analyse la plus simple..."
                    
                    # Utiliser le scanner avec configuration minimale
                    docker run --rm \\
                      -v $(pwd):/usr/src \\
                      --add-host=host.docker.internal:host-gateway \\
                      sonarsource/sonar-scanner-cli:latest \\
                      -Dsonar.projectKey=devops-maram-simple \\
                      -Dsonar.projectName="DevOps Maram Simple" \\
                      -Dsonar.host.url=http://host.docker.internal:9000 \\
                      -Dsonar.sources=src
                    
                    echo "✅ Méthode simple terminée"
                '''
            }
        }
        
        stage('Final Verification') {
            steps {
                echo "📊 VÉRIFICATION FINALE..."
                sh '''
                    echo "🎉 TOUTES LES MÉTHODES EXÉCUTÉES !"
                    echo ""
                    echo "🌐 OUVREZ SONARQUBE MAINTENANT:"
                    echo "   http://localhost:9000/projects"
                    echo ""
                    echo "🔍 CHERCHEZ CES PROJETS:"
                    echo "   • DevOps Maram Docker Method"
                    echo "   • DevOps Maram Simple" 
                    echo "   • DevOps Maram Real Project"
                    echo ""
                    echo "⏱️  Attendez 2-3 minutes puis rafraîchissez"
                    echo ""
                    echo "📋 SI RIEN N'APPARAÎT:"
                    echo "   1. Vérifiez les logs ci-dessus"
                    echo "   2. Vérifiez les logs SonarQube"
                    echo "   3. Testez manuellement (instructions suivantes)"
                '''
            }
        }
    }
}
