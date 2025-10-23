package tn.esprit.services;

import tn.esprit.entities.Order;
import tn.esprit.entities.Product;
import tn.esprit.entities.User;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements IGenericService<Order> {
    private final Connection conn;

    public OrderServiceImpl() {
        try {
            conn = DataBase.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    @Override
    public void add(Order order) {
        String sqlOrder = "INSERT INTO `order` (user_id, total, order_date) VALUES (?, ?, ?)";
        String sqlDetail = "INSERT INTO order_product(order_id, product_id) VALUES (?, ?)";
        double total = order.getProducts().stream().mapToDouble(Product::getPrice).sum();

        try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
            psOrder.setInt(1, order.getUser().getId());
            psOrder.setDouble(2, total);
            psOrder.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            psOrder.executeUpdate();

            int orderId;
            try (ResultSet rs = psOrder.getGeneratedKeys()) {
                orderId = rs.next() ? rs.getInt(1) : 0;
            }

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                for (Product p : order.getProducts()) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, p.getIdProduct());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Order order) {
        String sqlUpdate = "UPDATE `order` SET user_id=?, total=? WHERE idOrder=?";
        String sqlDelete = "DELETE FROM order_product WHERE order_id=?";
        String sqlDetail = "INSERT INTO order_product(order_id, product_id) VALUES (?, ?)";
        double total = order.getProducts().stream().mapToDouble(Product::getPrice).sum();

        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
             PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
             PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {

            // mise à jour de la commande
            psUpdate.setInt(1, order.getUser().getId());
            psUpdate.setDouble(2, total);
            psUpdate.setInt(3, order.getIdOrder());
            psUpdate.executeUpdate();

            // suppression des anciens produits
            psDelete.setInt(1, order.getIdOrder());
            psDelete.executeUpdate();

            // ajout des nouveaux produits
            for (Product p : order.getProducts()) {
                psDetail.setInt(1, order.getIdOrder());
                psDetail.setInt(2, p.getIdProduct());
                psDetail.addBatch();
            }
            psDetail.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String delDetail = "DELETE FROM order_product WHERE order_id=?";
        String delOrder = "DELETE FROM `order` WHERE idOrder=?";

        try (PreparedStatement psDelDetail = conn.prepareStatement(delDetail);
             PreparedStatement psDel = conn.prepareStatement(delOrder)) {

            psDelDetail.setInt(1, id);
            psDelDetail.executeUpdate();

            psDel.setInt(1, id);
            psDel.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String sqlOrder = "SELECT * FROM `order`";
        String sqlProd = "SELECT p.idProduct, p.name, p.price, p.idCategory " +
                "FROM product p JOIN order_product op ON p.idProduct = op.product_id WHERE op.order_id=?";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlOrder)) {

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");
                int userId = rs.getInt("user_id");
                User user = new User(userId, "", "");

                List<Product> products = new ArrayList<>();
                try (PreparedStatement psProd = conn.prepareStatement(sqlProd)) {
                    psProd.setInt(1, orderId);
                    try (ResultSet rsProd = psProd.executeQuery()) {
                        while (rsProd.next()) {
                            products.add(new Product(
                                    rsProd.getInt("idProduct"),
                                    rsProd.getString("name"),
                                    rsProd.getDouble("price"),
                                    null
                            ));
                        }
                    }
                }

                orders.add(new Order(orderId, user, products));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order getById(int id) {
        String sqlOrder = "SELECT * FROM `order` WHERE idOrder=?";
        String sqlProd = "SELECT p.idProduct, p.name, p.price, p.idCategory " +
                "FROM product p JOIN order_product op ON p.idProduct = op.product_id WHERE op.order_id=?";

        try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {
            psOrder.setInt(1, id);
            try (ResultSet rs = psOrder.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    User user = new User(userId, "", "");

                    List<Product> products = new ArrayList<>();
                    try (PreparedStatement psProd = conn.prepareStatement(sqlProd)) {
                        psProd.setInt(1, id);
                        try (ResultSet rsProd = psProd.executeQuery()) {
                            while (rsProd.next()) {
                                products.add(new Product(
                                        rsProd.getInt("idProduct"),
                                        rsProd.getString("name"),
                                        rsProd.getDouble("price"),
                                        null
                                ));
                            }
                        }
                    }

                    return new Order(id, user, products);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
