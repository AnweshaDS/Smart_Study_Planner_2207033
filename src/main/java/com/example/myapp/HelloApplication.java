package com.example.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        StageHolder.setStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cover.fxml"));
        Scene scene = new Scene(loader.load(), 900, 650);
        stage.setTitle("Smart Study Planner");
        stage.setScene(scene);
        stage.setResizable(true);       
        stage.setMinWidth(640);         
        stage.setMinHeight(480);
        stage.setMaximized(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
