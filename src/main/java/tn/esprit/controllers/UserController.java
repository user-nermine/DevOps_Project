package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserServiceImpl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @FXML
    private ListView<String> userListView;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private Button goToOrderButton;

    private final UserServiceImpl userService = new UserServiceImpl();

    @FXML
    public void handleGoToOrder() {
        try {
            var loader = new FXMLLoader(getClass().getResource("/tn.esprit.views/order.fxml"));
            var root = loader.load();
            var stage = (Stage) goToOrderButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Commandes");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la vue Order", e);
        }
    }

    @FXML
    public void initialize() {
        loadUsers();
    }

    private void loadUsers() {
        var items = FXCollections.observableArrayList();
        for (var user : userService.getAll()) {
            items.add(user.getId() + " - " + user.getUsername() + " (" + user.getEmail() + ")");
        }
        userListView.setItems(items);
    }

    @FXML
    private void handleAddUser() {
        var username = usernameField.getText().trim();
        var email = emailField.getText().trim();
        if (!username.isEmpty() && !email.isEmpty()) {
            var user = new User(0, username, email); // id = 0 car auto-increment en DB
            userService.add(user);
            loadUsers(); // recharger la liste
            usernameField.clear();
            emailField.clear();
        }
    }

    @FXML
    private void handleDeleteUser() {
        var selected = userListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                var id = Integer.parseInt(selected.split(" - ")[0]);
                userService.delete(id);
                loadUsers();
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la suppression : id invalide", e);
            }
        }
    }
}
