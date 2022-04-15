package com.example.russianCheckers;

import com.example.russianCheckers.ui.pages.WelcomePage;
import javafx.application.Application;
import javafx.stage.Stage;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) {
        WelcomePage.setStage(stage);
        WelcomePage.printWelcomePage();
    }

    public static void main(String[] args) {
        launch();
    }
}