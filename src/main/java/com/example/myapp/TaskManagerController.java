package com.example.myapp;

import com.example.myapp.dao.TaskDAO;
import com.example.myapp.model.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class TaskManagerController {

    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private TextField plannedField;

    @FXML private ListView<Task> todoList;
    @FXML private ListView<Task> runningList;
    @FXML private ListView<Task> pausedList;
    @FXML private ListView<Task> completedList;

    private final TaskDAO dao = new TaskDAO();

    @FXML
    public void initialize() {
        // Set custom cell factory for each ListView
        setupCellFactory(todoList);
        setupCellFactory(runningList);
        setupCellFactory(pausedList);
        setupCellFactory(completedList);

        // Load DB items
        Platform.runLater(this::refreshAll);
    }

    private void setupCellFactory(ListView<Task> lv) {
        lv.setCellFactory(list -> new ListCell<>() {
            private final HBox hb = new HBox(8);
            private final Label title = new Label();
            // Buttons inside each cell (optional) - for now just clickable selection and external buttons work
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    title.setText(item.getTitle() + " (" + item.getStatus() + ")");
                    hb.getChildren().setAll(title);
                    setGraphic(hb);
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
        } catch (NumberFormatException e) {
            planned = 0;
        }
        if (title.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Please enter a title");
            a.showAndWait();
            return;
        }
        Task t = new Task(0, title, desc, "TODO", planned, 0);
        dao.insert(t);
        titleField.clear();
        descField.clear();
        plannedField.clear();
        refreshAll();
    }

    private Task getSelected() {
        if (!todoList.getSelectionModel().isEmpty()) return todoList.getSelectionModel().getSelectedItem();
        if (!runningList.getSelectionModel().isEmpty()) return runningList.getSelectionModel().getSelectedItem();
        if (!pausedList.getSelectionModel().isEmpty()) return pausedList.getSelectionModel().getSelectedItem();
        if (!completedList.getSelectionModel().isEmpty()) return completedList.getSelectionModel().getSelectedItem();
        return null;
    }

    @FXML
    public void onStartSelected() {
        Task t = getSelected();
        if (t == null) return;
        t.setStatus("RUNNING");
        dao.update(t);
        refreshAll();
    }

    @FXML
    public void onPauseSelected() {
        Task t = getSelected();
        if (t == null) return;
        t.setStatus("PAUSED");
        dao.update(t);
        refreshAll();
    }

    @FXML
    public void onFinishSelected() {
        Task t = getSelected();
        if (t == null) return;
        t.setStatus("COMPLETED");
        dao.update(t);
        refreshAll();
    }

    @FXML
    public void onDeleteSelected() {
        Task t = getSelected();
        if (t == null) return;
        boolean ok = new Alert(Alert.AlertType.CONFIRMATION, "Delete \"" + t.getTitle() + "\"?").showAndWait().filter(b -> b==ButtonType.OK).isPresent();
        if (ok) {
            dao.delete(t.getId());
            refreshAll();
        }
    }

    @FXML
    public void onViewDB() {
        try {
            Stage stage = (Stage) todoList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/db_view.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackCover() {
        try {
            Stage stage = (Stage) todoList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/splash.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAll() {
        List<Task> todos = dao.getTasksByStatus("TODO");
        List<Task> running = dao.getTasksByStatus("RUNNING");
        List<Task> paused = dao.getTasksByStatus("PAUSED");
        List<Task> completed = dao.getTasksByStatus("COMPLETED");

        todoList.getItems().setAll(todos);
        runningList.getItems().setAll(running);
        pausedList.getItems().setAll(paused);
        completedList.getItems().setAll(completed);
    }
}
