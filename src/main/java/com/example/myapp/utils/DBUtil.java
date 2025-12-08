package com.example.myapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:studyplanner.db";

    static {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  title TEXT NOT NULL,
                  description TEXT,
                  status TEXT NOT NULL,
                  planned_time INTEGER DEFAULT 0,
                  spent_time INTEGER DEFAULT 0
                );
                """;
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
