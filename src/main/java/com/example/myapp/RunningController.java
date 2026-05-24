package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import com.example.myapp.utils.SceneUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RunningController {

    @FXML
    private ListView<Task> runningList;

    private final TaskDAO taskDAO = new TaskDAO();

    @FXML
    public void initialize() {
        refreshList();

        runningList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldTask, task) -> openPomodoro(task));
    }

    private void openPomodoro(Task task) {
        if (task == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/myapp/pomodoro.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 650);

            PomodoroController controller = loader.getController();
            controller.setTask(task);

            Stage stage = (Stage) runningList.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            showError("Failed to open Pomodoro screen.");
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        SceneUtil.switchTo(
                "/com/example/myapp/task_manager.fxml",
                StageHolder.getStage().getWidth(), StageHolder.getStage().getHeight()
        );
    }

    private void refreshList() {
        try {
            runningList.getItems().setAll(
                    taskDAO.getTasksByStatus(TaskStatus.RUNNING)
            );
        } catch (Exception e) {
            showError("Failed to fetch running tasks.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message != null ? message : "An unknown error occurred.");
        alert.showAndWait();
    }
}