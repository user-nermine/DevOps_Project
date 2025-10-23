package tn.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.utils.DataBase;

import static javafx.application.Application.launch;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        DataBase.initTables();
        var loader = new FXMLLoader(getClass().getResource("/tn.esprit.views/user.fxml"));
        var scene = new Scene(loader.load(), 50, 400);
        primaryStage.setTitle("Gestion Utilisateurs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // ✅ Appel à la méthode launch() de Application
    }
}
