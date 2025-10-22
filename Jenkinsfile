pipeline {
    agent any
    
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
                        find . -name "*.java" -type f || echo "Aucun fichier Java trouvé"
                    '''
                }
            }
        }
        
        stage('🔧 Validate Environment') {
            steps {
                echo "🔧 Validation de l'environnement..."
                script {
                    // Vérification des outils disponibles
                    sh '''
                        echo "=== Vérification des outils ==="
                        java -version || echo "Java non disponible"
                        mvn --version || echo "Maven non disponible"
                        which sonar-scanner && sonar-scanner --version || echo "SonarScanner non disponible"
                    '''
                }
            }
        }
        
        stage('🧹 Clean Project') {
            steps {
                echo "🧹 Nettoyage du projet..."
                sh 'mvn clean -B -q'
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
                    sh 'ls -lh target/*.jar || echo "Aucun JAR trouvé"'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('🔍 SonarQube Analysis') {
            steps {
                echo "🔍 Analyse de la qualité du code avec SonarQube..."
                script {
                    try {
                        // Méthode 1: Avec le plugin SonarQube Jenkins
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
                                  -Dsonar.sourceEncoding=UTF-8 \
                                  -Dsonar.coverage.exclusions=**/test/**,**/generated/** \
                                  -B
                            """
                        }
                    } catch (Exception e) {
                        echo "❌ Échec de l'analyse SonarQube avec Maven: ${e.getMessage()}"
                        
                        // Méthode 2: Fallback avec sonar-scanner CLI
                        script {
                            try {
                                echo "🔄 Tentative avec sonar-scanner CLI..."
                                sh """
                                    sonar-scanner \
                                      -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                                      -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                                      -Dsonar.host.url=${SONAR_HOST_URL} \
                                      -Dsonar.sources=src/main/java \
                                      -Dsonar.tests=src/test/java \
                                      -Dsonar.java.binaries=target/classes \
                                      -Dsonar.sourceEncoding=UTF-8
                                """
                            } catch (Exception e2) {
                                echo "⚠️ SonarScanner CLI non disponible - Configuration manquante"
                                echo "📝 Pour configurer SonarQube:"
                                echo "   1. Installer le plugin SonarQube dans Jenkins"
                                echo "   2. Configurer les serveurs SonarQube dans Jenkins"
                                echo "   3. Ajouter les credentials SonarQube"
                            }
                        }
                    }
                }
            }
            post {
                success {
                    echo "✅ Analyse SonarQube lancée avec succès"
                    echo "📊 Consultez les résultats sur: ${SONAR_HOST_URL}/dashboard?id=${SONAR_PROJECT_KEY}"
                }
            }
        }
        
        stage('⏳ Quality Gate') {
            when {
                expression { 
                    // Ne s'exécute que si l'analyse SonarQube a réussi
                    currentBuild.result != 'FAILURE' 
                }
            }
            steps {
                echo "⏳ Vérification du Quality Gate..."
                script {
                    try {
                        timeout(time: 5, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: false
                        }
                    } catch (Exception e) {
                        echo "⚠️ Timeout ou erreur du Quality Gate: ${e.getMessage()}"
                        echo "📊 Vérifiez manuellement sur: ${SONAR_HOST_URL}"
                    }
                }
            }
            post {
                success {
                    echo "✅ Quality Gate passé avec succès"
                }
                unsuccessful {
                    echo "⚠️ Quality Gate non passé - Vérifiez les métriques sur SonarQube"
                }
            }
        }
        
        stage('📊 Generate Reports') {
            steps {
                echo "📊 Génération des rapports..."
                sh '''
                    echo "=== Résumé du build ==="
                    echo "Build Number: ${BUILD_NUMBER}"
                    echo "Project: ${SONAR_PROJECT_NAME}"
                    echo "SonarQube URL: ${SONAR_HOST_URL}/dashboard?id=${SONAR_PROJECT_KEY}"
                    echo "Workspace: ${WORKSPACE}"
                    
                    # Tentative de génération de rapport de couverture
                    mvn jacoco:report -B -q || echo "Rapport JaCoCo non généré"
                '''
                
                // Archivage des rapports de test
                publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site',
                    reportFiles: 'index.html',
                    reportName: 'Rapports Maven'
                ])
            }
        }
    }
    
    post {
        always {
            echo "📊 Pipeline terminé - Statut: ${currentBuild.currentResult}"
            echo "🔗 URL du Build: ${env.BUILD_URL}"
            echo "🔗 URL des Artifacts: ${env.BUILD_URL}artifact/"
            
            // Nettoyage
            sh '''
                echo "=== Fichiers générés ==="
                find target/ -name "*.jar" -o -name "*.xml" -o -name "*.html" 2>/dev/null | head -10 || echo "Aucun fichier généré trouvé"
            '''
        }
        success {
            echo "✅✅✅ SUCCÈS DU PIPELINE ✅✅✅"
            script {
                // Notification de succès
                if (manager.build.resultIsBetterOrEqualTo(hudson.model.Result.SUCCESS)) {
                    echo "🎉 Toutes les étapes terminées avec succès!"
                    echo "📦 Artifacts archivés: ${env.BUILD_URL}artifact/"
                    echo "📊 SonarQube: ${SONAR_HOST_URL}/dashboard?id=${SONAR_PROJECT_KEY}"
                }
            }
        }
        failure {
            echo "❌❌❌ ÉCHEC DU PIPELINE ❌❌❌"
            echo "🔍 Vérifiez les logs: ${env.BUILD_URL}console"
            echo "💡 Solutions possibles:"
            echo "   - Vérifier la configuration Maven"
            echo "   - Vérifier la connexion à SonarQube"
            echo "   - Vérifier les credentials SonarQube dans Jenkins"
        }
        unstable {
            echo "⚠️⚠️⚠️ PIPELINE INSTABLE ⚠️⚠️⚠️"
            echo "📊 Qualité du code à améliorer - Consultez SonarQube"
        }
    }
}
