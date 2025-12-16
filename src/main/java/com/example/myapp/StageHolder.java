package com.example.myapp;

import javafx.stage.Stage;

public class StageHolder {
    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static Stage getStage() {
        return stage;
    }
}
