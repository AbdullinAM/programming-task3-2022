package com.example.russianCheckers;

import com.example.russianCheckers.ui.FieldUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) {
        FieldUI fieldUI = FieldUI.getFieldUI();
        fieldUI.setStage(stage);
        fieldUI.printWelcomePage();
    }

    public static void main(String[] args) {
        launch();
    }
}
