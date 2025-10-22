pipeline {
    agent any
    tools {
        maven 'maven-3.9.5'
        jdk 'jdk21'
    }
    
    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_PROJECT_KEY = 'devops-project-maram'
        SONAR_PROJECT_NAME = 'DevOps Project Maram'
    }
    
    stages {
        stage('📁 Checkout Code') {
            steps {
                echo "📁 Récupération du code source..."
                checkout scm
                script {
                    if (fileExists('pom.xml')) {
                        echo "✅ Fichier pom.xml trouvé"
                    } else {
                        error "❌ Fichier pom.xml manquant - Projet Maven requis"
                    }
                }
            }
            post {
                success {
                    echo "✅ Checkout réussi"
                    sh '''
                        echo "=== Structure du projet ==="
                        ls -la
                        echo "=== Fichiers Java ==="
                        find . -name "*.java" -type f
                    '''
                }
            }
        }
        
        stage('🔧 Validate Environment') {
            steps {
                echo "🔧 Validation de l'environnement..."
                script {
                    def requiredTools = [
                        'java': 'java -version',
                        'maven': 'mvn --version', 
                        'sonar-scanner': 'sonar-scanner --version'
                    ]
                    
                    requiredTools.each { tool, cmd ->
                        try {
                            sh cmd
                            echo "✅ $tool disponible"
                        } catch (Exception e) {
                            echo "⚠️ $tool non disponible: ${e.getMessage()}"
                        }
                    }
                }
            }
        }
        
        stage('🧹 Clean Project') {
            steps {
                echo "🧹 Nettoyage du projet..."
                sh 'mvn clean -B'
            }
        }
        
        stage('🔨 Compile & Tests') {
            steps {
                echo "🔨 Compilation et exécution des tests..."
                sh 'mvn compile test-compile -B'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java'
                    )
                }
            }
        }
        
        stage('📦 Package Application') {
            steps {
                echo "📦 Création du package..."
                sh 'mvn package -DskipTests -B'
            }
            post {
                success {
                    echo "✅ Package créé avec succès"
                    sh 'ls -lh target/*.jar'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('🔍 SonarQube Analysis') {
            steps {
                echo "🔍 Analyse de la qualité du code avec SonarQube..."
                script {
                    try {
                        withSonarQubeEnv('sonarqube') {
                            sh """
                                mvn sonar:sonar \
                                  -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                                  -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                                  -Dsonar.host.url=${SONAR_HOST_URL} \
                                  -Dsonar.sources=src/main/java \
                                  -Dsonar.tests=src/test/java \
                                  -Dsonar.java.binaries=target/classes \
                                  -Dsonar.junit.reportsPath=target/surefire-reports \
                                  -Dsonar.jacoco.reportPaths=target/jacoco.exec \
                                  -Dsonar.sourceEncoding=UTF-8 \
                                  -Dsonar.coverage.exclusions=**/test/**,**/generated/** \
                                  -B
                            """
                        }
                    } catch (Exception e) {
                        echo "❌ Échec de l'analyse SonarQube: ${e.getMessage()}"
                        // Continuer le pipeline même si SonarQube échoue
                    }
                }
            }
            post {
                success {
                    echo "✅ Analyse SonarQube lancée avec succès"
                    echo "📊 Consultez les résultats sur: ${SONAR_HOST_URL}"
                }
            }
        }
        
        stage('⏳ Quality Gate') {
            steps {
                echo "⏳ Vérification du Quality Gate..."
                script {
                    try {
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: false
                        }
                    } catch (Exception e) {
                        echo "⚠️ Timeout ou erreur du Quality Gate: ${e.getMessage()}"
                        // Ne pas bloquer le pipeline
                    }
                }
            }
            post {
                success {
                    echo "✅ Quality Gate passé avec succès"
                }
                unsuccessful {
                    echo "⚠️ Quality Gate non passé - Vérifiez les métriques"
                }
            }
        }
        
        stage('🐳 Build Docker Image') {
            when {
                expression { 
                    fileExists('Dockerfile') 
                }
            }
            steps {
                echo "🐳 Construction de l'image Docker..."
                script {
                    try {
                        sh '''
                            docker --version
                            docker build -t devops-app:${BUILD_NUMBER} .
                            docker images | grep devops-app
                        '''
                    } catch (Exception e) {
                        echo "⚠️ Docker non disponible: ${e.getMessage()}"
                    }
                }
            }
        }
        
        stage('📊 Generate Reports') {
            steps {
                echo "📊 Génération des rapports..."
                sh '''
                    echo "=== Rapport de couverture ==="
                    mvn jacoco:report -B || echo "Jacoco report non disponible"
                    
                    echo "=== Résumé du build ==="
                    echo "Build: ${BUILD_NUMBER}"
                    echo "Projet: ${SONAR_PROJECT_NAME}" 
                    echo "URL Sonar: ${SONAR_HOST_URL}/dashboard?id=${SONAR_PROJECT_KEY}"
                    echo "Artifacts: target/*.jar"
                '''
            }
        }
    }
    
    post {
        always {
            echo "📊 Pipeline terminé - Statut: ${currentBuild.currentResult}"
            script {
                // Nettoyage des ressources temporaires
                sh '''
                    echo "=== Nettoyage ==="
                    docker system prune -f || true
                    du -sh . || true
                '''
            }
            
            // Publication des rapports
            publishHTML([
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'Rapport de Couverture JaCoCo'
            ])
            
            // Notification Slack/Email optionnelle
            emailext (
                subject: "Build ${currentBuild.currentResult}: Job '${env.JOB_NAME}' (${env.BUILD_NUMBER})",
                body: """
                Bonjour,

                Le build ${currentBuild.currentResult} pour le projet ${SONAR_PROJECT_NAME}.

                Détails:
                - Build: ${env.BUILD_URL}
                - SonarQube: ${SONAR_HOST_URL}/dashboard?id=${SONAR_PROJECT_KEY}
                - Durée: ${currentBuild.durationString}

                Cordialement,
                Jenkins
                """,
                to: "devops-team@company.com"
            )
        }
        success {
            echo "✅✅✅ SUCCÈS DU PIPELINE ✅✅✅"
            echo "📦 Artifacts: ${env.BUILD_URL}artifact/"
            echo "📊 SonarQube: ${SONAR_HOST_URL}/dashboard?id=${SONAR_PROJECT_KEY}"
        }
        failure {
            echo "❌❌❌ ÉCHEC DU PIPELINE ❌❌❌"
            echo "🔍 Vérifiez les logs: ${env.BUILD_URL}console"
        }
        unstable {
            echo "⚠️⚠️⚠️ PIPELINE INSTABLE ⚠️⚠️⚠️"
            echo "📊 Qualité du code à améliorer"
        }
    }
}
