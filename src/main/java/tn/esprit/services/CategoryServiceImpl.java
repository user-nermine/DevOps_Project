package tn.esprit.services;

import tn.esprit.entities.Category;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements IGenericService<Category> {

    private Connection conn;

    public CategoryServiceImpl() {
        try {
            conn = DataBase.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Category c) {
        addCategory(c);
    }

    public void addCategory(Category c) {
        var sql = "INSERT INTO category (name) VALUES (?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Category c) {
        var sql = "UPDATE category SET name=? WHERE idCategory=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getIdCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        deleteCategory(id);
    }

    public void deleteCategory(int idCategory) {
        var sql = "DELETE FROM category WHERE idCategory=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategory);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Category getById(int idCategory) {
        Category category = null;
        var sql = "SELECT * FROM category WHERE idCategory=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategory);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = new Category(rs.getInt("idCategory"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public List<Category> getAll() {
        var list = new ArrayList<Category>();
        var sql = "SELECT * FROM category";
        try (var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("idCategory"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
