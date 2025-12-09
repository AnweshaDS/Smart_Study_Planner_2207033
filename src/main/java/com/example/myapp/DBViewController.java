package com.example.myapp;

import com.example.myapp.model.Task;
import com.example.myapp.utils.DBUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBViewController {

    @FXML private TableView<Task> table;

    @FXML private TableColumn<Task, Integer> colId;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDesc;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, Integer> colPlanned;
    @FXML private TableColumn<Task, Integer> colSpent;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPlanned.setCellValueFactory(new PropertyValueFactory<>("plannedTime"));
        colSpent.setCellValueFactory(new PropertyValueFactory<>("spentTime"));

        loadData();
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
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getInt("planned_time"),
                        rs.getInt("spent_time")
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/task_manager.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
