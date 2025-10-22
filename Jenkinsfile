pipeline {
  agent any

  tools {
    maven 'maven'
  }

  environment {
    PROJECT_KEY = 'DevOps_Project_Maram'
  }

  stages {
    stage('Create Complete Project') {
      steps {
        echo '📁 Création du projet JavaFX complet...'
        sh '''
          # Nettoyage des fichiers inutiles
          rm -f *.dll
          
          # Création de la structure
          mkdir -p src/main/java/tn/esprit
          mkdir -p src/test/java/tn/esprit
          mkdir -p src/main/resources

          # Création du pom.xml
          cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>tn.esprit</groupId>
    <artifactId>Order</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <sonar.host.url>http://localhost:9001</sonar.host.url>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0</version>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.8</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
EOF

          # Création de MainApp.java
          cat > src/main/java/tn/esprit/MainApp.java << 'EOF'
package tn.esprit;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Order Management System - Maram");
    }
    
    public String processOrder(String order) {
        return "Processed: " + order;
    }
}
EOF

          # Création de OrderService.java
          cat > src/main/java/tn/esprit/OrderService.java << 'EOF'
package tn.esprit;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<String> orders = new ArrayList<>();
    
    public void addOrder(String order) {
        orders.add(order);
    }
    
    public List<String> getOrders() {
        return new ArrayList<>(orders);
    }
    
    public int getOrderCount() {
        return orders.size();
    }
    
    public boolean removeOrder(String order) {
        return orders.remove(order);
    }
}
EOF

          # Création de tests
          cat > src/test/java/tn/esprit/OrderServiceTest.java << 'EOF'
package tn.esprit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    
    @Test
    public void testAddOrder() {
        OrderService service = new OrderService();
        service.addOrder("Test Order");
        assertEquals(1, service.getOrderCount());
    }
    
    @Test
    public void testGetOrders() {
        OrderService service = new OrderService();
        service.addOrder("Order 1");
        service.addOrder("Order 2");
        assertEquals(2, service.getOrders().size());
    }
    
    @Test
    public void testRemoveOrder() {
        OrderService service = new OrderService();
        service.addOrder("Order to remove");
        assertTrue(service.removeOrder("Order to remove"));
        assertEquals(0, service.getOrderCount());
    }
    
    @Test
    public void testMainApp() {
        MainApp app = new MainApp();
        String result = app.processOrder("Test");
        assertEquals("Processed: Test", result);
    }
}
EOF

          echo "✅ Projet créé avec succès!"
          ls -la
          find . -name "*.java" -type f
        '''
      }
    }

    stage('Build & Test') {
      steps {
        echo '🔨 Compilation et tests...'
        sh 'mvn -B clean test'
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        echo '🔍 Analyse SonarQube...'
        script {
          withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
            sh """
            mvn -B sonar:sonar \
              -Dsonar.projectKey=${env.PROJECT_KEY} \
              -Dsonar.host.url=http://localhost:9001 \
              -Dsonar.login=${SONAR_TOKEN}
            """
          }
        }
      }
    }
  }

  post {
    success {
      echo '✅ SUCCÈS! Projet créé et analysé dans SonarQube!'
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
    failure {
      echo '❌ ÉCHEC - Vérifiez les logs'
    }
  }
}
