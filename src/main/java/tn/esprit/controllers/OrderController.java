package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Order;
import tn.esprit.entities.Product;
import tn.esprit.entities.User;
import tn.esprit.services.OrderServiceImpl;
import tn.esprit.services.ProductServiceImpl;
import tn.esprit.services.UserServiceImpl;

import java.io.IOException;
import java.util.ArrayList;

public class OrderController {

    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ListView<Product> productListView;
    @FXML
    private ListView<Order> orderListView;

    private OrderServiceImpl orderService;
    private ProductServiceImpl productService;

    @FXML
    private void handleGoToProduct(ActionEvent event) {
        try {
            var loader = new FXMLLoader(getClass().getResource("/tn/esprit/views/product.fxml"));
            Parent root = (Parent) loader.load();
            var stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        orderService = new OrderServiceImpl();
        productService = new ProductServiceImpl();
        var userService = new UserServiceImpl();

        var users = FXCollections.observableArrayList(userService.getAll());
        userComboBox.setItems(users);

        var products = FXCollections.observableArrayList(productService.getAll());
        productListView.setItems(products);
        productListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        loadOrders();
    }

    private void loadOrders() {
        var orders = FXCollections.observableArrayList(orderService.getAll());
        orderListView.setItems(orders);
    }

    @FXML
    public void handleAddOrder() {
        var selectedUser = userComboBox.getSelectionModel().getSelectedItem();
        var selectedProducts = productListView.getSelectionModel().getSelectedItems();

        if (selectedUser == null || selectedProducts.isEmpty()) {
            var alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un utilisateur et au moins un produit !");
            alert.showAndWait();
            return;
        }

        var order = new Order(0, selectedUser, new ArrayList<>(selectedProducts));
        orderService.add(order);
        loadOrders();
    }

    @FXML
    public void handleDeleteOrder() {
        var selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderService.delete(selectedOrder.getIdOrder());
            loadOrders();
        }
    }
}
