package com.example.russianCheckers.ui.pages;

import com.example.russianCheckers.logic.CheckersLogic;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.russianCheckers.ui.settings.Settings.*;

public class GiveUpPage {

    public static void printGiveUpPage(Stage stage) {
        Group root = new Group();

        Text text = new Text();
        text.setText(GameFieldPage.getCurrentStepValue().getText() + " lost");
        text.setFont(Font.font(50));
        text.setX(SCREEN_RESOLUTION_X / 2 - 25);
        text.setY(SCREEN_RESOLUTION_Y / 2 - 50);
        text.setFill(Color.rgb(158, 114, 101));

        Rectangle welcomeButton = new Rectangle();
        welcomeButton.setFill(Color.rgb(46, 20, 12));
        welcomeButton.setHeight(100);
        welcomeButton.setWidth(300);
        welcomeButton.setArcHeight(30);
        welcomeButton.setArcWidth(30);
        welcomeButton.setX(SCREEN_RESOLUTION_X / 2 - 50);
        welcomeButton.setY(SCREEN_RESOLUTION_Y / 2);

        Text welcomeText = new Text();
        welcomeText.setText("Welcome Page");
        welcomeText.setFont(Font.font(30));
        welcomeText.setFill(Color.rgb(158, 114, 101));
        welcomeText.setX(SCREEN_RESOLUTION_X / 2 - 10);
        welcomeText.setY(SCREEN_RESOLUTION_Y / 2 + 60);


        Rectangle textBackGround = new Rectangle();
        textBackGround.setX(SCREEN_RESOLUTION_X / 2 - 200);
        textBackGround.setY(SCREEN_RESOLUTION_Y / 2 - 150);
        textBackGround.setWidth(600);
        textBackGround.setHeight(300);
        textBackGround.setArcWidth(30);
        textBackGround.setArcHeight(30);
        textBackGround.setFill(Color.rgb(52, 38, 33));


        EventHandler<MouseEvent> eventHandler = e -> {
            WelcomePage.printWelcomePage();
            CheckersLogic.restartGame();
        };
        welcomeText.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        welcomeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        root.getChildren().add(textBackGround);
        root.getChildren().add(welcomeButton);
        root.getChildren().add(text);
        root.getChildren().add(welcomeText);
        Scene scene = new Scene(root, SCREEN_RESOLUTION_X + 200, SCREEN_RESOLUTION_Y, Color.rgb(69, 44, 36));
        stage.setScene(scene);
        stage.setTitle(GAME_NAME);
        stage.setResizable(false);
        stage.show();
    }
}
