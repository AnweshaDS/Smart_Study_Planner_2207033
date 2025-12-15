package com.example.myapp;

import com.example.myapp.dao.UserDAO;
import com.example.myapp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignupController {

    @FXML private TextField username, email, fullName, age, qualification;
    @FXML private PasswordField password;

    private final UserDAO dao = new UserDAO();

    @FXML
    public void onRegister() {
        User u = new User(
                username.getText(),
                email.getText(),
                password.getText(),
                fullName.getText(),
                Integer.parseInt(age.getText()),
                qualification.getText()
        );

        if (dao.register(u)) {
            loadLogin();
        } else {
            new Alert(Alert.AlertType.ERROR, "Signup failed").show();
        }
    }

    private void loadLogin() {
        try {
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/com/example/myapp/login.fxml")),
                    900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
