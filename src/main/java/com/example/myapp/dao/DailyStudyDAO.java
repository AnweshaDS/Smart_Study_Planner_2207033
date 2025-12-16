package com.example.myapp.dao;

import com.example.myapp.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class DailyStudyDAO {

    public void addSeconds(int seconds) {
        if (seconds <= 0) return;

        String today = LocalDate.now().toString();

        String sql = """
            INSERT INTO daily_study (study_date, total_seconds)
            VALUES (?, ?)
            ON CONFLICT(study_date)
            DO UPDATE SET total_seconds = total_seconds + ?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, today);
            ps.setInt(2, seconds);
            ps.setInt(3, seconds);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTodayTotal() {
        String today = LocalDate.now().toString();

        String sql = "SELECT total_seconds FROM daily_study WHERE study_date = ?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, today);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_seconds");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
