package com.example.russianCheckers;

import com.example.russianCheckers.logic.Field;
import com.example.russianCheckers.logic.FieldModifier;
import com.example.russianCheckers.ui.FieldUI;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) {
        FieldUI fieldUI = FieldUI.getFieldUI();
        fieldUI.setStage(stage);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(fieldUI);
        fieldUI.printGameField();
    }

    public static void main(String[] args) {
        launch();
    }
}
