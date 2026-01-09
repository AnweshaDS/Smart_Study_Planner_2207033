package com.example.myapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final String DB =
            "jdbc:sqlite:" + System.getProperty("user.home") + "/studyplanner.db";

    static {
        try (Connection c = getConnection();
             Statement st = c.createStatement()) {

            st.execute("""
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                status INTEGER NOT NULL,
                target_seconds INTEGER,
                study_seconds INTEGER,
                break_seconds INTEGER,
                spent_seconds INTEGER,
                last_start_time INTEGER,
                today_spent_seconds INTEGER,
                last_study_date TEXT
            );
        """);

            st.execute("""
            CREATE TABLE IF NOT EXISTS daily_study (
                study_date TEXT PRIMARY KEY,
                total_seconds INTEGER NOT NULL
            );
        """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB);
    }
}
