package com.example.myapp;

import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import com.example.myapp.utils.DBUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBViewController {

    @FXML private TableView<Task> table;

    @FXML private TableColumn<Task, String> colId;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, String> colTarget;
    @FXML private TableColumn<Task, String> colSpent;
    @FXML private TableColumn<Task, String> colRemaining;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId()))
        );

        colTitle.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTitle())
        );

        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(statusText(c.getValue().getStatus()))
        );

        colTarget.setCellValueFactory(c ->
                new SimpleStringProperty(
                        Task.format(c.getValue().getTargetSeconds())
                )
        );

        colSpent.setCellValueFactory(c ->
                new SimpleStringProperty(
                        Task.format(c.getValue().getSpentSeconds())
                )
        );

        colRemaining.setCellValueFactory(c -> {
            int remaining = Math.max(
                    0,
                    c.getValue().getTargetSeconds()
                            - c.getValue().getSpentSeconds()
            );
            return new SimpleStringProperty(Task.format(remaining));
        });

        loadData();
    }

    private String statusText(int status) {
        return switch (status) {
            case TaskStatus.TODO -> "TODO";
            case TaskStatus.RUNNING -> "RUNNING";
            case TaskStatus.PAUSED -> "PAUSED";
            case TaskStatus.COMPLETED -> "COMPLETED";
            default -> "UNKNOWN";
        };
    }

    private void loadData() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tasks")) {

            var list = FXCollections.<Task>observableArrayList();

            while (rs.next()) {
                list.add(new Task(
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
                ));
            }

            table.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) table.getScene().getWindow();
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/com/example/myapp/task_manager.fxml"
                    ));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
