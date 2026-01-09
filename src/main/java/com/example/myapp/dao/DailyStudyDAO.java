package com.example.myapp.dao;

import com.example.myapp.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DailyStudyDAO {

    private static final String TODAY = LocalDate.now().toString();

    //add seconds

    public void addSeconds(int sec) {
        if (sec <= 0) return;

        if (existsToday()) {
            update(sec);
        } else {
            insert(sec);
        }
    }

    //read

    public static int getTodayTotal() {

        String sql = """
            SELECT total_seconds
            FROM daily_study
            WHERE study_date = ?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, TODAY);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_seconds");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    //helpers

    private boolean existsToday() {

        String sql = """
            SELECT 1 FROM daily_study
            WHERE study_date = ?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, TODAY);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void insert(int sec) {

        String sql = """
            INSERT INTO daily_study (study_date, total_seconds)
            VALUES (?, ?)
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, TODAY);
            ps.setInt(2, sec);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(int sec) {

        String sql = """
            UPDATE daily_study
            SET total_seconds = total_seconds + ?
            WHERE study_date = ?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sec);
            ps.setString(2, TODAY);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
