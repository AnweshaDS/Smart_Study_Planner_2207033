package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class CompletedController {

    @FXML
    private ListView<Task> completedList;

    private final TaskDAO dao = new TaskDAO();

    @FXML
    public void initialize() {
        setupCellFactory();
        refresh();
    }

    private void setupCellFactory() {
        completedList.setCellFactory(lv -> new ListCell<>() {

            private final HBox box = new HBox(10);
            private final Label titleLabel = new Label();
            private final Label timeLabel = new Label();

            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(item.getTitle());
                    timeLabel.setText(formatSeconds(item.getSpentTime()));

                    box.getChildren().setAll(titleLabel, timeLabel);
                    setGraphic(box);
                }
            }
        });
    }

    private String formatSeconds(int totalSeconds) {
        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;

        return String.format(" | %02d:%02d:%02d", h, m, s);
    }

    @FXML
    public void onDelete() {
        Task t = completedList.getSelectionModel().getSelectedItem();
        if (t == null) return;

        boolean ok = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete \"" + t.getTitle() + "\"?"
        ).showAndWait().filter(b -> b == ButtonType.OK).isPresent();

        if (ok) {
            dao.delete(t.getId());
            refresh();
        }
    }

    private void refresh() {
        List<Task> completed = dao.getTasksByStatus("COMPLETED");
        completedList.getItems().setAll(completed);
    }

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) completedList.getScene().getWindow();
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/com/example/myapp/task_manager.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
