pipeline {
    agent any
    
    stages {
        stage('DEBUG - System Check') {
            steps {
                echo "🔍 DEBUG COMPLET DU SYSTÈME"
                sh '''
                    echo "=== ENVIRONNEMENT JENKINS ==="
                    echo "PWD: $(pwd)"
                    echo "User: $(whoami)"
                    echo "=== RÉSEAU ==="
                    echo "Test localhost:"
                    curl -s http://localhost:9000/api/system/status | head -2 || echo "❌ Localhost inaccessible"
                    echo "=== DOCKER ==="
                    docker --version || echo "❌ Docker non disponible"
                    echo "=== FICHIERS ==="
                    find . -name "*.java" 2>/dev/null | head -5 || echo "Aucun fichier Java"
                    echo "=== FIN DEBUG ==="
                '''
            }
        }
        
        stage('Create SIMPLE Project') {
            steps {
                echo "🛠️ Création projet ULTRA-SIMPLE"
                sh '''
                    # Un seul fichier, structure minimale
                    rm -rf simple-project
                    mkdir -p simple-project/src
                    
                    cat > simple-project/src/Main.java << 'EOF'
public class Main {
    public static void main(String[] args) {
        System.out.println("Test SonarQube");
    }
}
EOF
                    echo "✅ Fichier créé:"
                    cat simple-project/src/Main.java
                '''
            }
        }
        
        stage('METHOD 1: Direct Docker Scan') {
            steps {
                echo "🔍 MÉTHODE 1: Scan direct"
                sh '''
                    cd simple-project
                    echo "🎯 TEST 1: Scanner basique..."
                    
                    docker run --rm \
                      -v $(pwd):/usr/src \
                      sonarsource/sonar-scanner-cli:latest \
                      -Dsonar.projectKey=test-method-1 \
                      -Dsonar.projectName="Test Method 1" \
                      -Dsonar.host.url=http://host.docker.internal:9000 \
                      -Dsonar.sources=src \
                      -Dsonar.sourceEncoding=UTF-8 \
                      -Dsonar.scm.disabled=true
                      
                    echo "🔄 Test 1 terminé"
                '''
            }
        }
        
        stage('METHOD 2: Network Host') {
            steps {
                echo "🔍 MÉTHODE 2: Network host"
                sh '''
                    cd simple-project
                    echo "🎯 TEST 2: Network host..."
                    
                    docker run --rm \
                      --network=host \
                      -v $(pwd):/usr/src \
                      sonarsource/sonar-scanner-cli:latest \
                      -Dsonar.projectKey=test-method-2 \
                      -Dsonar.projectName="Test Method 2" \
                      -Dsonar.host.url=http://localhost:9000 \
                      -Dsonar.sources=src \
                      -Dsonar.scm.disabled=true
                      
                    echo "🔄 Test 2 terminé"
                '''
            }
        }
        
        stage('METHOD 3: Manual API Test') {
            steps {
                echo "🔍 MÉTHODE 3: Test API manuel"
                sh '''
                    echo "🎯 TEST 3: Appel API direct..."
                    
                    # Tester la création de projet via API
                    curl -X POST "http://localhost:9000/api/projects/create" \
                      -u admin:admin \
                      -d "project=test-api-manual" \
                      -d "name=Test API Manual" \
                      -d "visibility=public" || echo "❌ API create failed"
                      
                    echo "✅ Test API effectué"
                '''
            }
        }
        
        stage('Check SonarQube Logs') {
            steps {
                echo "📋 Vérification logs SonarQube"
                sh '''
                    echo "🔍 Dernières lignes des logs SonarQube:"
                    # Essayer différentes méthodes pour voir les logs
                    docker logs sonarqube 2>&1 | tail -10 || echo "❌ Impossible de lire les logs Docker"
                    
                    echo "📊 Statut SonarQube:"
                    curl -s http://localhost:9000/api/system/status
                    echo ""
                '''
            }
        }
        
        stage('FINAL VERIFICATION') {
            steps {
                echo "🎯 VÉRIFICATION FINALE"
                sh '''
                    echo "=== RÉSULTATS ATTENDUS ==="
                    echo "Allez sur: http://localhost:9000/projects"
                    echo ""
                    echo "Vous devriez voir:"
                    echo "1. 'Test Method 1'"
                    echo "2. 'Test Method 2'" 
                    echo "3. 'Test API Manual'"
                    echo ""
                    echo "=== SI RIEN N'APPARAÎT ==="
                    echo "Le problème est:"
                    echo "❌ SonarQube ne reçoit PAS les analyses"
                    echo "❌ Problème réseau entre Docker et SonarQube"
                    echo "❌ Configuration SonarQube incorrecte"
                    echo ""
                    echo "=== SOLUTION ALTERNATIVE ==="
                    echo "1. Redémarrer SonarQube: docker restart sonarqube"
                    echo "2. Vérifier le port 9000: netstat -an | grep 9000"
                    echo "3. Tester avec un autre scanner"
                '''
            }
        }
    }
}
