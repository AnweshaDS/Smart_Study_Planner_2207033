package com.example.myapp;

import com.example.myapp.dao.DailyStudyDAO;
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

    private final TaskDAO taskDAO = new TaskDAO();
    private final DailyStudyDAO dailyDAO = new DailyStudyDAO();

    private Timeline timer;
    private Task activeTask;

    private int sessionSeconds = 0; // ✅ TRACK CURRENT SESSION

    @FXML
    public void initialize() {
        runningList.getItems().setAll(taskDAO.getTasksByStatus("RUNNING"));

        runningList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, selected) -> startTimer(selected));
    }

    private void startTimer(Task task) {
        stopTimer();

        if (task == null) return;

        activeTask = task;
        sessionSeconds = 0;

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            activeTask.setSpentTime(activeTask.getSpentTime() + 1);
            sessionSeconds++;
            taskDAO.update(activeTask);
            updateLabel(activeTask.getSpentTime());
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        updateLabel(activeTask.getSpentTime());
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void updateLabel(int totalSeconds) {
        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;

        timerLabel.setText(String.format("%02d:%02d:%02d", h, m, s));
    }

    @FXML
    public void pauseTask() {
        if (activeTask == null) return;

        stopTimer();
        dailyDAO.addSeconds(sessionSeconds); // ✅ UPDATE DAILY TOTAL

        activeTask.setStatus("PAUSED");
        taskDAO.update(activeTask);

        runningList.getItems().remove(activeTask);
        reset();
    }

    @FXML
    public void finishTask() {
        if (activeTask == null) return;

        stopTimer();
        dailyDAO.addSeconds(sessionSeconds); // ✅ UPDATE DAILY TOTAL

        activeTask.setStatus("COMPLETED");
        taskDAO.update(activeTask);

        runningList.getItems().remove(activeTask);
        reset();
    }

    @FXML
    public void deleteTask() {
        if (activeTask == null) return;

        stopTimer();
        taskDAO.delete(activeTask.getId());
        reset();
    }

    private void reset() {
        activeTask = null;
        sessionSeconds = 0;
        timerLabel.setText("00:00:00");
    }

    @FXML
    public void goBack() {
        stopTimer();
        com.example.myapp.SceneUtil.switchTo("/com/example/myapp/task_manager.fxml", 900, 650);
    }
}
