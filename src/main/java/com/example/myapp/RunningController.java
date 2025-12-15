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

public class RunningController {

    @FXML private ListView<Task> runningList;

    private final TaskDAO dao = new TaskDAO();

    @FXML
    public void initialize() {
        setupCellFactory();
        refresh();
    }

    private void setupCellFactory() {
        runningList.setCellFactory(lv -> new ListCell<>() {
            private final HBox box = new HBox(8);
            private final Label label = new Label();

            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.getTitle() + " | " + item.getPlannedTime() + " min");
                    box.getChildren().setAll(label);
                    setGraphic(box);
                }
            }
        });
    }

    @FXML
    public void onPause() {
        Task t = runningList.getSelectionModel().getSelectedItem();
        if (t == null) return;

        t.setStatus("PAUSED");
        dao.update(t);
        refresh();
    }

    @FXML
    public void onFinish() {
        Task t = runningList.getSelectionModel().getSelectedItem();
        if (t == null) return;

        t.setStatus("COMPLETED");
        dao.update(t);
        refresh();
    }

    @FXML
    public void onDelete() {
        Task t = runningList.getSelectionModel().getSelectedItem();
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
        List<Task> running = dao.getTasksByStatus("RUNNING");
        runningList.getItems().setAll(running);
    }

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) runningList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/task_manager.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
