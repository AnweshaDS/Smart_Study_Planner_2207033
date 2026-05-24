package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import com.example.myapp.utils.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class PausedController {

    @FXML private ListView<Task> pausedList;
    private final TaskDAO taskDAO = new TaskDAO();

    @FXML
    public void initialize() {
        refreshList();
    }

    private void refreshList() {
        pausedList.getItems().setAll(taskDAO.getTasksByStatus(TaskStatus.PAUSED));
    }

    @FXML
    public void onRun() {
        Task task = pausedList.getSelectionModel().getSelectedItem();
        if (task == null) {
            showInfo("No task selected", "Please select a paused task to run.");
            return;
        }
        task.setStatus(TaskStatus.RUNNING);
        taskDAO.update(task);
        SceneUtil.switchTo("/com/example/myapp/running.fxml", StageHolder.getStage().getWidth(), StageHolder.getStage().getHeight());
    }

    @FXML
    public void onFinish() {
        Task task = pausedList.getSelectionModel().getSelectedItem();
        if (task == null) {
            showInfo("No task selected", "Please select a task to finish.");
            return;
        }
        task.setStatus(TaskStatus.COMPLETED);
        taskDAO.update(task);
        SceneUtil.switchTo("/com/example/myapp/task_manager.fxml", StageHolder.getStage().getWidth(), StageHolder.getStage().getHeight());
    }

    @FXML
    public void onDelete() {
        Task task = pausedList.getSelectionModel().getSelectedItem();
        if (task == null) {
            showInfo("No task selected", "Please select a task to delete.");
            return;
        }
        taskDAO.delete(task.getId());
        refreshList();
    }

    @FXML
    public void onBack() {
        SceneUtil.switchTo("/com/example/myapp/task_manager.fxml", StageHolder.getStage().getWidth(), StageHolder.getStage().getHeight());
    }

    private void showInfo(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}