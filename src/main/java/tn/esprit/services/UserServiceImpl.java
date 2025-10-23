package tn.esprit.services;

import tn.esprit.entities.User;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements IGenericService<User> {
    private Connection conn;

    public UserServiceImpl() {
        try {
            conn = DataBase.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            // tu peux éventuellement afficher un message ou arrêter l'app
        }
    }

   @Override
    public void add(User user) {
        var sql = "INSERT INTO user(username, email) VALUES(?, ?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        var sql = "UPDATE user SET username=?, email=? WHERE id=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        var sql = "DELETE FROM user WHERE id=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getById(int id) {
        var sql = "SELECT * FROM user WHERE id=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        var list = new ArrayList<User>();
        var sql = "SELECT * FROM user";
        try (var st = conn.createStatement();
             var rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}