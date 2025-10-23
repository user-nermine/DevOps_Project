package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBase {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "orderdb";
    private static final String USER = "root";

    // ❌ Mot de passe en clair supprimé
    // ✅ Chargement sécurisé via variable d'environnement
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    /**
     * Retourne une connexion à la base de données.
     * Crée la base si elle n'existe pas.
     */
    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isEmpty()) {
            throw new SQLException("Mot de passe MySQL manquant : définissez la variable d'environnement DB_PASSWORD");
        }

        // Connexion sécurisée
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);

        try (Connection conn = DriverManager.getConnection(URL, props);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }

        return DriverManager.getConnection(URL + DB_NAME, props);
    }

    /**
     * Crée toutes les tables si elles n'existent pas.
     */
    public static void initTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String userSql = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100) NOT NULL" +
                    ");";
            stmt.executeUpdate(userSql);

            String categorySql = "CREATE TABLE IF NOT EXISTS category (" +
                    "idCategory INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL" +
                    ");";
            stmt.executeUpdate(categorySql);

            String productSql = "CREATE TABLE IF NOT EXISTS product (" +
                    "idProduct INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "idCategory INT," +
                    "FOREIGN KEY (idCategory) REFERENCES category(idCategory) ON DELETE SET NULL" +
                    ");";
            stmt.executeUpdate(productSql);

            String orderSql = "CREATE TABLE IF NOT EXISTS `order` (" +
                    "idOrder INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "total DOUBLE," +
                    "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE" +
                    ");";
            stmt.executeUpdate(orderSql);

            String orderProductSql = "CREATE TABLE IF NOT EXISTS order_product (" +
                    "order_id INT," +
                    "product_id INT," +
                    "PRIMARY KEY(order_id, product_id)," +
                    "FOREIGN KEY (order_id) REFERENCES `order`(idOrder) ON DELETE CASCADE," +
                    "FOREIGN KEY (product_id) REFERENCES product(idProduct) ON DELETE CASCADE" +
                    ");";
            stmt.executeUpdate(orderProductSql);

            System.out.println("Base et toutes les tables créées ou déjà existantes.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
