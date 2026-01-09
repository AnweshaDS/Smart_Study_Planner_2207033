package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import com.example.myapp.utils.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class PausedController {

    @FXML private ListView<Task> pausedList;

    private final TaskDAO taskDAO = new TaskDAO();

    @FXML
    public void initialize() {
        refreshList();
    }

    private void refreshList() {
        pausedList.getItems().setAll(
                taskDAO.getTasksByStatus(TaskStatus.PAUSED)
        );
    }

    //actions

    @FXML
    public void onRun() {
        Task task = pausedList.getSelectionModel().getSelectedItem();
        if (task == null) return;

        task.setStatus(TaskStatus.RUNNING);
        taskDAO.update(task);

        SceneUtil.switchTo(
                "/com/example/myapp/running.fxml",
                900, 650
        );
    }

    @FXML
    public void onFinish() {
        Task task = pausedList.getSelectionModel().getSelectedItem();
        if (task == null) return;

        task.setStatus(TaskStatus.COMPLETED);
        taskDAO.update(task);

        refreshList();
    }

    @FXML
    public void onDelete() {
        Task task = pausedList.getSelectionModel().getSelectedItem();
        if (task == null) return;

        taskDAO.delete(task.getId());
        refreshList();
    }

    @FXML
    public void onBack() {
        SceneUtil.switchTo(
                "/com/example/myapp/task_manager.fxml",
                900, 650
        );
    }
}
