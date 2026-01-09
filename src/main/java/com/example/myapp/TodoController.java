package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import com.example.myapp.model.TaskStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class TodoController {

    @FXML private TextField titleField;
    @FXML private TextField targetField;
    @FXML private TextField studyField;
    @FXML private TextField breakField;
    @FXML private ListView<Task> todoList;

    private final TaskDAO dao = new TaskDAO();

    // lifecycle

    @FXML
    public void initialize() {
        setupCellFactory();
        refresh();
    }

    // UI

    private void setupCellFactory() {
        todoList.setCellFactory(lv -> new ListCell<>() {

            private final HBox box = new HBox(10);
            private final Label label = new Label();

            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);

                if (empty || task == null) {
                    setGraphic(null);
                } else {
                    label.setText(
                            task.getTitle()
                                    + " | Target: "
                                    + Task.format(task.getTargetSeconds())
                    );
                    box.getChildren().setAll(label);
                    setGraphic(box);
                }
            }
        });
    }

    // actions

    @FXML
    public void onAddTask() {

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            alert("Task title required");
            return;
        }

        int targetSec = (int) parseHMS(targetField.getText());
        int studySec  = (int) parseHMS(studyField.getText());
        int breakSec  = (int) parseHMS(breakField.getText());

        if (studySec <= 0 || breakSec <= 0) {
            alert("Study and Break time must be > 0");
            return;
        }

        Task task = new Task(
                0,
                title,
                TaskStatus.TODO,
                targetSec,
                studySec,
                breakSec,
                0,
                0L,
                0,
                null
        );

        dao.insert(task);

        titleField.clear();
        targetField.clear();
        studyField.clear();
        breakField.clear();

        refresh();
    }

    @FXML
    public void onStart() {

        Task t = todoList.getSelectionModel().getSelectedItem();
        if (t == null) return;

        t.setStatus(TaskStatus.RUNNING);
        t.setLastStartTime(System.currentTimeMillis());

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

    // helpers

    private void refresh() {
        List<Task> tasks = dao.getTasksByStatus(TaskStatus.TODO);
        todoList.getItems().setAll(tasks);
    }

    private long parseHMS(String text) {
        if (text == null || text.isBlank()) return 0;

        try {
            String[] p = text.trim().split(":");
            if (p.length != 3) return 0;

            return Long.parseLong(p[0]) * 3600
                    + Long.parseLong(p[1]) * 60
                    + Long.parseLong(p[2]);
        } catch (Exception e) {
            return 0;
        }
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    // navigation

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) todoList.getScene().getWindow();
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/com/example/myapp/task_manager.fxml"
                    ));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
