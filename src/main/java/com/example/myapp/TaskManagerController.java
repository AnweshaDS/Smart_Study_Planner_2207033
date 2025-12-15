package com.example.myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TaskManagerController {

    private void loadPage(String fxml, double w, double h) {
        try {
            Stage stage = (Stage) Stage.getWindows().filtered(Window -> Window.isShowing()).get(0);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            stage.setScene(new Scene(loader.load(), w, h));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openTodo() {
        loadPage("/com/example/myapp/todo.fxml", 900, 650);
    }

    @FXML
    public void openRunning() {
        loadPage("/com/example/myapp/running.fxml", 900, 650);
    }

    @FXML
    public void openPaused() {
        loadPage("/com/example/myapp/paused.fxml", 900, 650);
    }

    @FXML
    public void openCompleted() {
        loadPage("/com/example/myapp/completed.fxml", 900, 650);
    }

    @FXML
    public void onViewDB() {
        loadPage("/com/example/myapp/db_view.fxml", 900, 650);
    }

    @FXML
    public void onBackCover() {
        loadPage("/com/example/myapp/cover.fxml", 900, 650);
    }
}
