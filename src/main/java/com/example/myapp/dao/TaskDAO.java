package com.example.myapp.dao;

import com.example.myapp.model.Task;
import com.example.myapp.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public Task insert(Task t) {
        String sql = "INSERT INTO tasks(title,description,status,planned_time,spent_time) VALUES(?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getTitle());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getStatus());
            ps.setInt(4, t.getPlannedTime());
            ps.setInt(5, t.getSpentTime());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) t.setId(rs.getInt(1));
            }
            return t;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean update(Task t) {
        String sql = "UPDATE tasks SET title=?,description=?,status=?,planned_time=?,spent_time=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getTitle());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getStatus());
            ps.setInt(4, t.getPlannedTime());
            ps.setInt(5, t.getSpentTime());
            ps.setInt(6, t.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Task> getTasksByStatus(String status) {
        String sql = "SELECT * FROM tasks WHERE status = ?";
        List<Task> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<Task> getAll() {
        String sql = "SELECT * FROM tasks ORDER BY id DESC";
        List<Task> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public Task findById(int id) {
        String sql = "SELECT * FROM tasks WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Task mapRow(ResultSet rs) throws SQLException {
        return new Task(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getInt("planned_time"),
                rs.getInt("spent_time")
        );
    }
}
