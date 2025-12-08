package com.example.myapp;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private ListView<String> taskList;

    @FXML
    private TextField taskInput;

    // Add Task
    @FXML
    protected void onAddTask() {
        String task = taskInput.getText().trim();
        if (!task.isEmpty()) {
            taskList.getItems().add(task + " (Not Started)");
            taskInput.clear();
        }
    }

    // Start Task
    @FXML
    protected void onStartTask() {
        int index = taskList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            String task = taskList.getItems().get(index);
            taskList.getItems().set(index, updateStatus(task, "Started"));
        }
    }

    // Pause Task
    @FXML
    protected void onPauseTask() {
        int index = taskList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            String task = taskList.getItems().get(index);
            taskList.getItems().set(index, updateStatus(task, "Paused"));
        }
    }

    // Finish Task
    @FXML
    protected void onFinishTask() {
        int index = taskList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            String task = taskList.getItems().get(index);
            taskList.getItems().set(index, updateStatus(task, "Finished"));
        }
    }

    private String updateStatus(String original, String newStatus) {
        String taskName = original.split("\\(")[0].trim();
        return taskName + " (" + newStatus + ")";
    }
}
