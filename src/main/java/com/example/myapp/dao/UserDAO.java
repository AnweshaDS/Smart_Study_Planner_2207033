package com.example.myapp.dao;

import com.example.myapp.model.User;
import com.example.myapp.utils.DBUtil;

import java.sql.*;

public class UserDAO {

    public boolean register(User u) {
        String sql = """
            INSERT INTO users(username,email,password,full_name,age,qualification)
            VALUES(?,?,?,?,?,?)
        """;
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getFullName());
            ps.setInt(5, u.getAge());
            ps.setString(6, u.getQualification());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String userOrEmail, String password) {
        String sql = """
            SELECT * FROM users
            WHERE (username=? OR email=?) AND password=?
        """;
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userOrEmail);
            ps.setString(2, userOrEmail);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getString("qualification")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
