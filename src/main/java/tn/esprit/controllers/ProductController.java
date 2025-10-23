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
import tn.esprit.entities.Category;
import tn.esprit.entities.Product;
import tn.esprit.services.CategoryServiceImpl;
import tn.esprit.services.IGenericService;
import tn.esprit.services.ProductServiceImpl;

import java.io.IOException;

public class ProductController {

    private IGenericService<Product> productService = new ProductServiceImpl();
    private CategoryServiceImpl categoryService = new CategoryServiceImpl();

    @FXML
    private ListView<Product> productListView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private void handleGoToCategory(ActionEvent event) {
        try {
            var loader = new FXMLLoader(getClass().getResource("/tn.esprit/views/category.fxml"));
            Parent root = (Parent) loader.load();
            var stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        loadCategoriesFromDB();
        loadProducts();

        categoryComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });
        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });
    }

    private void loadProducts() {
        var productList = FXCollections.observableArrayList(productService.getAll());
        productListView.setItems(productList);
    }

    private void loadCategoriesFromDB() {
        var categoryList = FXCollections.observableArrayList(categoryService.getAll());
        categoryComboBox.setItems(categoryList);
    }

    @FXML
    private void handleAddProduct() {
        var name = nameField.getText();
        var priceText = priceField.getText();
        var selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || priceText.isEmpty() || selectedCategory == null) {
            var alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            var alert = new Alert(Alert.AlertType.ERROR, "Le prix doit être un nombre valide !");
            alert.showAndWait();
            return;
        }

        var product = new Product(0, name, price, selectedCategory);
        productService.add(product);
        loadProducts();
        nameField.clear();
        priceField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeleteProduct() {
        var selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            var alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un produit !");
            alert.showAndWait();
            return;
        }
        productService.delete(selectedProduct.getIdProduct());
        loadProducts();
    }
}
