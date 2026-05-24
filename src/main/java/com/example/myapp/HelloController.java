package com.example.myapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class HelloController {
    @FXML private TextField taskInput;
    @FXML private ListView<String> taskList;

    @FXML private TextField timerInput;
    @FXML private Label timerLabel;

    private Timeline timeline;
    private int targetSeconds = 0;
    private int timeLeft = 0;

    @FXML
    public void initialize() {
        timerLabel.setText("00:00");
    }

    @FXML
    public void onAddTask() {
        String task = taskInput.getText();
        if (!task.isEmpty()) {
            taskList.getItems().add(task);
            taskInput.clear();
        }
    }

    @FXML
    public void onSetTimer() {
        try {
            targetSeconds = Integer.parseInt(timerInput.getText());
            if (targetSeconds <= 0) throw new NumberFormatException();
            timeLeft = targetSeconds;
            timerLabel.setText(formatTime(timeLeft));
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid, positive number of seconds.");
        }
    }

    @FXML
    public void onStartTask() {
        if (targetSeconds <= 0) {
            showAlert(Alert.AlertType.WARNING, "No Timer Set", "Set the timer before starting.");
            return;
        }
        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText(formatTime(timeLeft));
            if (timeLeft <= 0) {
                timeline.stop();
                timerLabel.setText("00:00");
                showNotification();
            }
        }));
        timeline.setCycleCount(timeLeft);
        timeline.play();
    }

    @FXML
    public void onPauseTask() {
        if (timeline != null) timeline.pause();
    }

    @FXML
    public void onFinishTask() {
        if (timeline != null) timeline.stop();
        timerLabel.setText(formatTime(targetSeconds));
        timeLeft = targetSeconds;
    }

    // Helper methods
    private String formatTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type, content, ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }

    private void showNotification() {
        showAlert(Alert.AlertType.INFORMATION, "Time's Up!", "Your target time is over! Take a break or start another task.");

        // Optional: System notification (requires Java AWT, may not work in all environments)
        try {
            if (java.awt.SystemTray.isSupported()) {
                java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
                java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage("");
                java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "Smart Study Planner");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
                trayIcon.displayMessage("Time's Up!", "Your target time is over! Take a break or start another task.", java.awt.TrayIcon.MessageType.INFO);
                tray.remove(trayIcon); // Clean up instantly
            }
        } catch (Exception ignored) {}
    }
}