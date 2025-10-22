pipeline {
    agent any
    
    environment {
        SONAR_HOST = 'localhost:9000'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "📁 Récupération du code..."
                git branch: 'maram', url: 'https://github.com/user-nermine/DevOps_Project.git'
            }
        }
        
        stage('Create Windows Compatible Project') {
            steps {
                echo "🛠️ Création projet compatible Windows..."
                bat '''
                    @echo off
                    echo Création de la structure du projet...
                    
                    rmdir /s /q my-sonar-project 2>nul
                    mkdir my-sonar-project
                    mkdir my-sonar-project\\src
                    mkdir my-sonar-project\\src\\main
                    mkdir my-sonar-project\\src\\main\\java
                    mkdir my-sonar-project\\src\\main\\java\\com
                    mkdir my-sonar-project\\src\\main\\java\\com\\devops
                    
                    cd my-sonar-project
                    
                    echo Création des fichiers Java...
                    
                    echo package com.devops; > src\\main\\java\\com\\devops\\MainApp.java
                    echo. >> src\\main\\java\\com\\devops\\MainApp.java
                    echo public class MainApp { >> src\\main\\java\\com\\devops\\MainApp.java
                    echo     public static void main(String[] args) { >> src\\main\\java\\com\\devops\\MainApp.java
                    echo         System.out.println("Hello SonarQube from Jenkins Windows!"); >> src\\main\\java\\com\\devops\\MainApp.java
                    echo         Calculator calc = new Calculator(); >> src\\main\\java\\com\\devops\\MainApp.java
                    echo         System.out.println("5 + 3 = " + calc.add(5, 3)); >> src\\main\\java\\com\\devops\\MainApp.java
                    echo     } >> src\\main\\java\\com\\devops\\MainApp.java
                    echo } >> src\\main\\java\\com\\devops\\MainApp.java
                    
                    echo package com.devops; > src\\main\\java\\com\\devops\\Calculator.java
                    echo. >> src\\main\\java\\com\\devops\\Calculator.java
                    echo public class Calculator { >> src\\main\\java\\com\\devops\\Calculator.java
                    echo     public int add(int a, int b) { >> src\\main\\java\\com\\devops\\Calculator.java
                    echo         return a + b; >> src\\main\\java\\com\\devops\\Calculator.java
                    echo     } >> src\\main\\java\\com\\devops\\Calculator.java
                    echo     >> src\\main\\java\\com\\devops\\Calculator.java
                    echo     public int multiply(int a, int b) { >> src\\main\\java\\com\\devops\\Calculator.java
                    echo         return a * b; >> src\\main\\java\\com\\devops\\Calculator.java
                    echo     } >> src\\main\\java\\com\\devops\\Calculator.java
                    echo } >> src\\main\\java\\com\\devops\\Calculator.java
                    
                    echo Projet créé avec succès!
                    dir src\\main\\java\\com\\devops\\
                '''
            }
        }
        
        stage('SonarQube Analysis - Windows Method') {
            steps {
                echo "🔍 Analyse SonarQube pour Windows..."
                script {
                    bat '''
                        @echo off
                        echo Lancement de l analyse SonarQube...
                        cd my-sonar-project
                        
                        echo Configuration SonarQube...
                        echo sonar.projectKey=devops-maram-windows > sonar-project.properties
                        echo sonar.projectName=DevOps Maram Windows >> sonar-project.properties
                        echo sonar.projectVersion=1.0 >> sonar-project.properties
                        echo sonar.sources=src/main/java >> sonar-project.properties
                        echo sonar.sourceEncoding=UTF-8 >> sonar-project.properties
                        echo sonar.host.url=http://localhost:9000 >> sonar-project.properties
                        
                        echo Fichiers a analyser:
                        dir src\\main\\java\\com\\devops\\ /B
                        
                        echo Execution du scanner SonarQube...
                        docker run --rm -v "%CD%":/usr/src sonarsource/sonar-scanner-cli:latest ^
                          -Dsonar.projectKey=devops-maram-windows ^
                          -Dsonar.projectName="DevOps Maram Windows" ^
                          -Dsonar.host.url=http://host.docker.internal:9000 ^
                          -Dsonar.sources=src/main/java ^
                          -Dsonar.sourceEncoding=UTF-8
                          
                        echo Analyse SonarQube TERMINEE!
                    '''
                }
            }
        }
        
        stage('Verify Analysis') {
            steps {
                echo "📊 Vérification..."
                bat '''
                    @echo off
                    echo.
                    echo ==================================
                    echo ✅ ANALYSE SONARQUBE EFFECTUEE!
                    echo ==================================
                    echo.
                    echo 🌐 OUVREZ SONARQUBE MAINTENANT:
                    echo    http://localhost:9000
                    echo.
                    echo 🔍 CHERCHEZ LE PROJET:
                    echo    "DevOps Maram Windows"
                    echo.
                    echo 📍 URL DIRECTE:
                    echo    http://localhost:9000/dashboard?id=devops-maram-windows
                    echo.
                    echo ⏱️  Attendez 1-2 minutes puis rafraichissez la page
                    echo ==================================
                '''
            }
        }
    }
    
    post {
        success {
            echo "🎉🎉🎉 SUCCÈS ! VÉRIFIEZ SONARQUBE ! 🎉🎉🎉"
        }
    }
}
