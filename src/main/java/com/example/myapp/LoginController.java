package com.example.myapp;

import com.example.myapp.dao.UserDAO;
import com.example.myapp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField userField;
    @FXML private PasswordField passField;

    private final UserDAO dao = new UserDAO();

    @FXML
    public void onLogin() {
        User u = dao.login(userField.getText(), passField.getText());
        if (u == null) {
            new Alert(Alert.AlertType.ERROR, "Invalid login").show();
            return;
        }
        loadTaskManager();
    }

    @FXML
    public void onSignup() {
        load("/com/example/myapp/signup.fxml");
    }

    private void loadTaskManager() {
        load("/com/example/myapp/task_manager.fxml");
    }

    private void load(String fxml) {
        try {
            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource(fxml)), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
