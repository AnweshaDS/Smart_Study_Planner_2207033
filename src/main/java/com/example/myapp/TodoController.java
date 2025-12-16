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

public class TodoController {

    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private TextField plannedField;
    @FXML private ListView<Task> todoList;

    private final TaskDAO dao = new TaskDAO();

    @FXML
    public void initialize() {
        setupCellFactory();
        refresh();
    }

    private void setupCellFactory() {
        todoList.setCellFactory(lv -> new ListCell<>() {
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
    public void onAddTask() {
        String title = titleField.getText().trim();
        String desc = descField.getText().trim();
        int planned = 0;


        try {
            if (!plannedField.getText().isBlank())
                planned = Integer.parseInt(plannedField.getText().trim());
        } catch (NumberFormatException ignored) {}

        if (title.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Task title required").showAndWait();
            return;
        }

        Task t = new Task(0, title, desc, "TODO", planned, 0);
        dao.insert(t);

        titleField.clear();
        descField.clear();
        plannedField.clear();

        refresh();
    }

    @FXML
    public void onStart() {
        Task t = todoList.getSelectionModel().getSelectedItem();
        if (t == null) return;

        t.setStatus("RUNNING");
        dao.update(t);
        refresh();
    }

    @FXML
    public void onDelete() {
        Task t = todoList.getSelectionModel().getSelectedItem();
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
        List<Task> todos = dao.getTasksByStatus("TODO");
        todoList.getItems().setAll(todos);
    }

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) todoList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/task_manager.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
