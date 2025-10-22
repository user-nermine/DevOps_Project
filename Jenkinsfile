pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo "📁 Récupération du code..."
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }

        stage('Create Simple Project') {
            steps {
                sh '''
                    echo "🛠️ Création du projet..."
                    rm -rf simple-app
                    mkdir -p simple-app/src/main/java
                    
                    cat > simple-app/src/main/java/Main.java << 'EOF'
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello SonarQube from Jenkins!");
    }
    
    public String process(String input) {
        return input != null ? input.toUpperCase() : "NULL";
    }
}
EOF
                    echo "✅ Projet créé"
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "🔍 Analyse SonarQube..."
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        cd simple-app
                        echo "🚀 Lancement de l'analyse..."
                        
                        # Utiliser le scanner SonarQube directement
                        sonar-scanner \
                          -Dsonar.projectKey=jenkins-auto-project-${BUILD_NUMBER} \
                          -Dsonar.projectName="Jenkins Auto Project ${BUILD_NUMBER}" \
                          -Dsonar.sources=src/main/java \
                          -Dsonar.sourceEncoding=UTF-8
                        
                        echo "✅ Analyse envoyée à SonarQube"
                    '''
                }
            }
        }

        stage('Verify') {
            steps {
                echo "📊 Vérification..."
                sh '''
                    echo "🎉 ANALYSE TERMINÉE !"
                    echo ""
                    echo "🌐 OUVREZ SONARQUBE MAINTENANT :"
                    echo "   http://localhost:9000/projects"
                    echo ""
                    echo "🔍 CHERCHEZ : 'Jenkins Auto Project ${BUILD_NUMBER}'"
                    echo ""
                    echo "⏱️ Attendez 30 secondes et rafraîchissez"
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
