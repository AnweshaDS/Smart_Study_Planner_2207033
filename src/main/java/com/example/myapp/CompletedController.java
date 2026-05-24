package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.util.List;

public class CompletedController {

    @FXML private ListView<Task> completedList;

    private final TaskDAO dao = new TaskDAO();

    @FXML
    public void initialize() {
        refresh();
    }

    @FXML
    public void onDelete() {
        Task t = completedList.getSelectionModel().getSelectedItem();
        if (t == null) {
            showInfo("No task selected", "Please select a completed task to delete.");
            return;
        }

        boolean ok = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete \"" + t.getTitle() + "\"?"
        ).showAndWait().filter(b -> b == javafx.scene.control.ButtonType.OK).isPresent();

        if (ok) {
            dao.delete(t.getId());
            refresh();
        }
    }

    private void refresh() {
        List<Task> tasks = dao.getTasksByStatus(TaskStatus.COMPLETED);
        completedList.getItems().setAll(tasks);
    }

    @FXML
    public void onBack() {
        StageHolder.getStage().getScene().setRoot(
                completedList.getScene().getRoot()
        );
        
    }

    private void showInfo(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}