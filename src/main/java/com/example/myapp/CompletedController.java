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

    @FXML private ListView<Task> completedList;

    private final TaskDAO dao = new TaskDAO();

    @FXML
    public void initialize() {
        setupCellFactory();
        refresh();
    }

    private void setupCellFactory() {
        completedList.setCellFactory(lv -> new ListCell<>() {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/task_manager.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
