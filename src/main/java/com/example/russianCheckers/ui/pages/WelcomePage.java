package com.example.russianCheckers.ui.pages;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.russianCheckers.ui.settings.Settings.SCREEN_RESOLUTION_X;
import static com.example.russianCheckers.ui.settings.Settings.SCREEN_RESOLUTION_Y;

public class WelcomePage {

    private static Stage stage;

    public static void printWelcomePage() {
        Group root = new Group();
        Rectangle rectangle = new Rectangle();

        rectangle.setHeight(300);
        rectangle.setWidth(500);
        rectangle.setFill(Color.rgb(52, 38, 33));
        rectangle.setArcWidth(50);
        rectangle.setArcHeight(50);
        rectangle.setX(SCREEN_RESOLUTION_X / 2 - 150);
        rectangle.setY(SCREEN_RESOLUTION_Y / 2 - 150);

        Rectangle buttonNewGame = new Rectangle();
        Text textGame = new Text();
        textGame.setText("Start game");
        textGame.setFont(Font.font(30));
        textGame.setFill(Color.rgb(158, 114, 101));
        textGame.setX(SCREEN_RESOLUTION_X / 2 + 10);
        textGame.setY(SCREEN_RESOLUTION_Y / 2 - 5);
        buttonNewGame.setHeight(75);
        buttonNewGame.setWidth(300);
        buttonNewGame.setFill(Color.rgb(46, 20, 12));
        buttonNewGame.setArcWidth(75);
        buttonNewGame.setArcHeight(75);
        buttonNewGame.setX(SCREEN_RESOLUTION_X / 2 - 50);
        buttonNewGame.setY(SCREEN_RESOLUTION_Y / 2 - 50);

        EventHandler<MouseEvent> eventHandler = e -> GameFieldPage.printGameField(stage);
        buttonNewGame.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        textGame.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        root.getChildren().add(rectangle);
        root.getChildren().add(buttonNewGame);
        root.getChildren().add(textGame);
        Scene scene = new Scene(root, SCREEN_RESOLUTION_X + 200, SCREEN_RESOLUTION_Y, Color.rgb(69, 44, 36));
        stage.setScene(scene);
        stage.setTitle("Russian checkers");
        stage.setResizable(false);
        stage.show();
    }

    public static void setStage(Stage stage) {
        WelcomePage.stage = stage;
    }
}
