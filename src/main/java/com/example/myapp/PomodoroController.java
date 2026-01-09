package com.example.myapp;

import com.example.myapp.dao.DailyStudyDAO;
import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import com.example.myapp.utils.SceneUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PomodoroController {

    @FXML private Label taskTitleLabel;
    @FXML private Label timerLabel;
    @FXML private Label modeLabel;

    private final TaskDAO taskDAO = new TaskDAO();
    private final DailyStudyDAO dailyDAO = new DailyStudyDAO();

    private Task task;
    private Timeline timer;

    private boolean isStudyPhase = true;
    private boolean isRunning = true;

    private int remainingSeconds;
    private long sessionStartMillis;

    //called externally

    public void setTask(Task task) {
        this.task = task;

        taskTitleLabel.setText(task.getTitle());
        startStudy();
    }

    // timer logic

    private void startStudy() {
        isStudyPhase = true;
        modeLabel.setText("STUDY");
        startTimer(task.getStudySeconds());
    }

    private void startBreak() {
        isStudyPhase = false;
        modeLabel.setText("BREAK");
        startTimer(task.getBreakSeconds());
    }

    private void startTimer(int seconds) {
        stopTimer();

        remainingSeconds = seconds;
        sessionStartMillis = System.currentTimeMillis();

        updateLabel();

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> tick())
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void tick() {
        remainingSeconds--;

        if (remainingSeconds <= 0) {
            saveTime();

            if (isStudyPhase) {
                startBreak();
            } else {
                startStudy();
            }
            return;
        }

        updateLabel();
    }

    private void updateLabel() {
        timerLabel.setText(Task.format(remainingSeconds));
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    // time saving

    private void saveTime() {
        long now = System.currentTimeMillis();
        int deltaSec = (int) ((now - sessionStartMillis) / 1000);

        if (deltaSec <= 0) return;

        task.setSpentSeconds(task.getSpentSeconds() + deltaSec);
        task.setTodaySpentSeconds(task.getTodaySpentSeconds() + deltaSec);

        taskDAO.update(task);
        dailyDAO.addSeconds(deltaSec);

        sessionStartMillis = now;
    }

    // actions

    @FXML
    public void pause() {
        if (!isRunning) {
            sessionStartMillis = System.currentTimeMillis();
            timer.play();
        } else {
            stopTimer();
            saveTime();
        }
        isRunning = !isRunning;
    }

    @FXML
    public void finish() {
        stopTimer();
        saveTime();

        task.setStatus(TaskStatus.COMPLETED);
        taskDAO.update(task);

        SceneUtil.switchTo(
                "/com/example/myapp/task_manager.fxml",
                900, 650
        );
    }

    @FXML
    public void onBack() {
        stopTimer();
        saveTime();

        task.setStatus(TaskStatus.PAUSED);
        taskDAO.update(task);

        SceneUtil.switchTo(
                "/com/example/myapp/task_manager.fxml",
                900, 650
        );
    }
}
