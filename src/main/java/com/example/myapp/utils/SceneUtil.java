package com.example.myapp.utils;

import com.example.myapp.StageHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SceneUtil {

    public static void switchTo(String fxml, double w, double h) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(fxml));
            StageHolder.getStage().setScene(new Scene(loader.load(), w, h));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
