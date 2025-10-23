package tn.esprit.services;

import tn.esprit.entities.Category;
import tn.esprit.entities.Product;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements IGenericService<Product> {

    private Connection conn;

    public ProductServiceImpl() {
        try {
            conn = DataBase.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   @Override
    public void add(Product product) {
        var sql = "INSERT INTO product (name, price, idCategory) VALUES (?, ?, ?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getCategory().getIdCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        var sql = "UPDATE product SET name=?, price=?, idCategory=? WHERE idProduct=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getCategory().getIdCategory());
            ps.setInt(4, product.getIdProduct());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idProduct) {
        var sql = "DELETE FROM product WHERE idProduct=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduct);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
        var products = new ArrayList<Product>();
        var sql = "SELECT p.idProduct, p.name, p.price, c.idCategory, c.name AS category_name " +
                  "FROM product p JOIN category c ON p.idCategory = c.idCategory";

        try (var st = conn.createStatement();
             var rs = st.executeQuery(sql)) {

            while (rs.next()) {
                var cat = new Category(rs.getInt("idCategory"), rs.getString("category_name"));
                var p = new Product(rs.getInt("idProduct"), rs.getString("name"), rs.getDouble("price"), cat);
                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public Product getById(int idProduct) {
        var sql = "SELECT p.idProduct, p.name, p.price, c.idCategory, c.name AS category_name " +
                  "FROM product p JOIN category c ON p.idCategory = c.idCategory WHERE p.idProduct=?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduct);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    var cat = new Category(rs.getInt("idCategory"), rs.getString("category_name"));
                    return new Product(rs.getInt("idProduct"), rs.getString("name"), rs.getDouble("price"), cat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}