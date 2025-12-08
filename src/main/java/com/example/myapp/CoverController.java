package com.example.myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class CoverController {
    @FXML
    private ImageView logoView;

    @FXML
    public void initialize() {
        // Try to load image from /images/logo.png - user can replace this file
        try (InputStream is = getClass().getResourceAsStream("logo1.png")) {
            if (is != null) {
                logoView.setImage(new Image(is));
            } else {
                // leave blank if not present; you can set a placeholder via CSS or leave the label below visible
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onContinue() {
        try {
            Stage stage = (Stage) logoView.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/task_manager.fxml"));
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
