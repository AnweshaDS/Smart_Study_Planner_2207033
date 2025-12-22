package com.example.myapp;

import com.example.myapp.dao.DailyStudyDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TaskManagerController {

    @FXML private Label dailyTotalLabel;

    private final DailyStudyDAO dailyDAO = new DailyStudyDAO();

    @FXML
    public void initialize() {
        updateDailyTotal();
    }

    private void updateDailyTotal() {
        int total = dailyDAO.getTodayTotal();

        int h = total / 3600;
        int m = (total % 3600) / 60;
        int s = total % 60;

        dailyTotalLabel.setText(
                String.format("Today Studied: %02d:%02d:%02d", h, m, s)
        );
    }

    private void switchScene(String fxml, double w, double h) {
        try {
            Stage stage = StageHolder.getStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            stage.setScene(new Scene(loader.load(), w, h));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void openTodo()      { switchScene("/com/example/myapp/todo.fxml", 900, 650); }
    @FXML public void openRunning()   { switchScene("/com/example/myapp/running.fxml", 900, 650); }
    @FXML public void openPaused()    { switchScene("/com/example/myapp/paused.fxml", 900, 650); }
    @FXML public void openCompleted() { switchScene("/com/example/myapp/completed.fxml", 900, 650); }
    @FXML public void onViewDB()       { switchScene("/com/example/myapp/db_view.fxml", 900, 600); }
    @FXML public void onLogout()       { switchScene("/com/example/myapp/cover.fxml", 900, 650); }
}
