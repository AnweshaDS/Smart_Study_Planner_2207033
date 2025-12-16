package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;

public class RunningController {

    @FXML private ListView<Task> runningList;
    @FXML private Label timerLabel;

    private final TaskDAO dao = new TaskDAO();
    private Timeline timer;
    private Task activeTask;

    @FXML
    public void initialize() {
        runningList.getItems().setAll(dao.getTasksByStatus("RUNNING"));

        runningList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTask, newTask) -> startTimer(newTask)
        );

        if (!runningList.getItems().isEmpty()) {
            runningList.getSelectionModel().selectFirst();
        } else {
            timerLabel.setText("00:00:00");
        }
    }

    private void startTimer(Task task) {
        stopTimer();

        if (task == null) return;

        activeTask = task;

        // Timeline ticks every second
        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    activeTask.setSpentTime(activeTask.getSpentTime() + 1);
                    updateLabel();
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        updateLabel();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void updateLabel() {
        if (activeTask == null) {
            timerLabel.setText("00:00:00");
            return;
        }

        int totalSeconds = activeTask.getSpentTime();

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        timerLabel.setText(
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
        );
    }

    @FXML
    public void pauseTask() {
        if (activeTask == null) return;

        stopTimer();
        activeTask.setStatus("PAUSED");

        dao.update(activeTask);

        runningList.getItems().remove(activeTask);
        activeTask = null;
        timerLabel.setText("00:00:00");
    }

    @FXML
    public void finishTask() {
        if (activeTask == null) return;

        stopTimer();
        activeTask.setStatus("COMPLETED");

        dao.update(activeTask);

        runningList.getItems().remove(activeTask);
        activeTask = null;
        timerLabel.setText("00:00:00");
    }

    @FXML
    public void deleteTask() {
        if (activeTask == null) return;

        stopTimer();
        dao.delete(activeTask.getId());

        runningList.getItems().remove(activeTask);
        activeTask = null;
        timerLabel.setText("00:00:00");
    }

    @FXML
    public void goBack() {
        stopTimer();

        if (activeTask != null) {
            dao.update(activeTask);
        }

        com.example.myapp.SceneUtil.switchTo("/com/example/myapp/task_manager.fxml", 900, 650);
    }
}
