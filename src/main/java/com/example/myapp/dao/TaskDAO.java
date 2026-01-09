package com.example.myapp.dao;

import com.example.myapp.model.Task;
import com.example.myapp.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public TaskDAO() {
        createTable();
    }

    // ---------- table ----------

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                status INTEGER NOT NULL,

                target_seconds INTEGER NOT NULL,
                study_seconds INTEGER NOT NULL,
                break_seconds INTEGER NOT NULL,

                spent_seconds INTEGER NOT NULL DEFAULT 0,
                last_start_time INTEGER NOT NULL DEFAULT 0,

                today_spent_seconds INTEGER NOT NULL DEFAULT 0,
                last_study_date TEXT
            )
        """;

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CRUD

    public void insert(Task t) {
        String sql = """
            INSERT INTO tasks (
                title, status,
                target_seconds, study_seconds, break_seconds,
                spent_seconds, last_start_time,
                today_spent_seconds, last_study_date
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getTitle());
            ps.setInt(2, t.getStatus());

            ps.setInt(3, t.getTargetSeconds());
            ps.setInt(4, t.getStudySeconds());
            ps.setInt(5, t.getBreakSeconds());

            ps.setInt(6, t.getSpentSeconds());
            ps.setLong(7, t.getLastStartTime());

            ps.setInt(8, t.getTodaySpentSeconds());
            ps.setString(9, t.getLastStudyDate());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Task t) {
        String sql = """
            UPDATE tasks SET
                title=?,
                status=?,

                target_seconds=?,
                study_seconds=?,
                break_seconds=?,

                spent_seconds=?,
                last_start_time=?,

                today_spent_seconds=?,
                last_study_date=?
            WHERE id=?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getTitle());
            ps.setInt(2, t.getStatus());

            ps.setInt(3, t.getTargetSeconds());
            ps.setInt(4, t.getStudySeconds());
            ps.setInt(5, t.getBreakSeconds());

            ps.setInt(6, t.getSpentSeconds());
            ps.setLong(7, t.getLastStartTime());

            ps.setInt(8, t.getTodaySpentSeconds());
            ps.setString(9, t.getLastStudyDate());

            ps.setInt(10, t.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("DELETE FROM tasks WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- queries ----------

    public List<Task> getTasksByStatus(int status) {
        List<Task> list = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE status=?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(fromRS(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Task getById(int id) {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("SELECT * FROM tasks WHERE id=?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return fromRS(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // mapper

    private Task fromRS(ResultSet rs) throws SQLException {
        return new Task(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("status"),

                rs.getInt("target_seconds"),
                rs.getInt("study_seconds"),
                rs.getInt("break_seconds"),

                rs.getInt("spent_seconds"),
                rs.getLong("last_start_time"),

                rs.getInt("today_spent_seconds"),
                rs.getString("last_study_date")
        );
    }
}
