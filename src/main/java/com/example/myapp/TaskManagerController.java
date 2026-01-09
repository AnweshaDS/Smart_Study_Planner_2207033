package com.example.myapp;

import com.example.myapp.dao.DailyStudyDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TaskManagerController {

    @FXML
    private Label dailyTotalLabel;

    private final DailyStudyDAO dailyDAO = new DailyStudyDAO();

    // lifecycle

    @FXML
    public void initialize() {
        refreshDailyTotal();
    }

    // daily total

    private void refreshDailyTotal() {
        int totalSeconds = Math.toIntExact(DailyStudyDAO.getTodayTotal());

        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;

        dailyTotalLabel.setText(
                String.format("Today Studied: %02d:%02d:%02d", h, m, s)
        );
    }

    /**
     * Call this when returning to TaskManager
     * from Running / Paused / Completed pages
     */
    public void onSceneShown() {
        refreshDailyTotal();
    }

    // navigation

    private void switchScene(String fxml, double width, double height) {
        try {
            Stage stage = StageHolder.getStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            stage.setScene(new Scene(loader.load(), width, height));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openTodo() {
        switchScene("/com/example/myapp/todo.fxml", 900, 650);
    }

    @FXML
    public void openRunning() {
        switchScene("/com/example/myapp/running.fxml", 900, 650);
    }

    @FXML
    public void openPaused() {
        switchScene("/com/example/myapp/paused.fxml", 900, 650);
    }

    @FXML
    public void openCompleted() {
        switchScene("/com/example/myapp/completed.fxml", 900, 650);
    }

    @FXML
    public void onViewDB() {
        switchScene("/com/example/myapp/db_view.fxml", 900, 600);
    }

    @FXML
    public void onLogout() {
        switchScene("/com/example/myapp/cover.fxml", 900, 650);
    }
}
