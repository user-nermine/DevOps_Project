pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://host.docker.internal:9000'
    }

    stages {
        stage('Test Docker') {
            steps {
                sh '''
                    echo "🐳 Test de Docker..."
                    docker --version && echo "✅ Docker disponible" || echo "❌ Docker non disponible"
                    
                    echo "🔗 Test connexion SonarQube..."
                    docker run --rm curlimages/curl:latest curl -s http://host.docker.internal:9000/api/system/status && echo "✅ SonarQube accessible" || echo "❌ SonarQube inaccessible"
                '''
            }
        }

        stage('Checkout') {
            steps {
                echo "📁 Récupération du code..."
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }

        stage('Create Project') {
            steps {
                sh '''
                    echo "🛠️ Création du projet..."
                    rm -rf app-project
                    mkdir -p app-project/src
                    
                    # Fichier Java simple
                    cat > app-project/src/Main.java << 'EOF'
public class Main {
    public static void main(String[] args) {
        System.out.println("Application DevOps");
    }
    
    public String process(String input) {
        return input != null ? input.toUpperCase() : "NULL";
    }
    
    public int calculate(int a, int b) {
        return a + b;
    }
}
EOF
                    echo "✅ Projet créé"
                '''
            }
        }

        stage('SonarQube Analysis with Docker') {
            steps {
                echo "🔍 Analyse SonarQube avec Docker..."
                sh '''
                    cd app-project
                    echo "🚀 Lancement de l'analyse..."
                    
                    # Scanner avec Docker
                    docker run --rm \
                      -v $(pwd):/usr/src \
                      sonarsource/sonar-scanner-cli:latest \
                      -Dsonar.projectKey=devops-docker-project \
                      -Dsonar.projectName="DevOps Docker Project" \
                      -Dsonar.host.url=http://host.docker.internal:9000 \
                      -Dsonar.sources=src \
                      -Dsonar.sourceEncoding=UTF-8 \
                      -Dsonar.scm.disabled=true
                    
                    echo "✅ Analyse COMPLÈTEMENT terminée"
                '''
            }
        }

        stage('Verify') {
            steps {
                echo "📊 Vérification..."
                sh '''
                    echo "🎉 ANALYSE RÉUSSIE !"
                    echo ""
                    echo "🌐 OUVREZ SONARQUBE :"
                    echo "   http://localhost:9000/projects"
                    echo ""
                    echo "🔍 CHERCHEZ : 'DevOps Docker Project'"
                    echo ""
                    echo "📍 URL : http://localhost:9000/dashboard?id=devops-docker-project"
                '''
                script {
                    sleep(30)
                }
            }
        }
    }

    post {
        success {
            echo "✅✅✅ SUCCÈS ! VÉRIFIEZ SONARQUBE ! ✅✅✅"
        }
    }
}
