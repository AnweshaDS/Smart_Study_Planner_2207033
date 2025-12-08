package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.util.List;

public class DBViewController {

    @FXML private TableView<Task> table;
    @FXML private TableColumn<Task, Integer> colId;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDesc;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, Integer> colPlanned;
    @FXML private TableColumn<Task, Integer> colSpent;

    private final TaskDAO dao = new TaskDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPlanned.setCellValueFactory(new PropertyValueFactory<>("plannedTime"));
        colSpent.setCellValueFactory(new PropertyValueFactory<>("spentTime"));
        refresh();
    }

    private void refresh() {
        List<Task> all = dao.getAll();
        table.setItems(FXCollections.observableArrayList(all));
    }

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) table.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task_manager.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
