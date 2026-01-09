package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CompletedController {

    @FXML private ListView<Task> completedList;

    private final TaskDAO dao = new TaskDAO();

    //  lifecycle

    @FXML
    public void initialize() {
        refresh();
    }

    //  actions

    @FXML
    public void onDelete() {
        Task t = completedList.getSelectionModel().getSelectedItem();
        if (t == null) return;

        boolean ok = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete \"" + t.getTitle() + "\"?"
        ).showAndWait().filter(b -> b == ButtonType.OK).isPresent();

        if (ok) {
            dao.delete(t.getId());
            refresh();
        }
    }

    // helpers

    private void refresh() {
        List<Task> tasks = dao.getTasksByStatus(TaskStatus.COMPLETED);
        completedList.getItems().setAll(tasks);
    }

    // navigation

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) completedList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/myapp/task_manager.fxml")
            );
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
