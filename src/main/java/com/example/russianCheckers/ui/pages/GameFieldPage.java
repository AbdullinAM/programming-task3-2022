package com.example.russianCheckers.ui.pages;

import com.example.russianCheckers.logic.CheckersLogic;
import com.example.russianCheckers.logic.Field;
import com.example.russianCheckers.ui.uiSettings.Settings;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.russianCheckers.ui.uiSettings.Settings.*;

public class GameFieldPage {

    private static final Text countStepsValue = new Text();
    private static final Text currentStepValue = new Text();
    private static final Text whiteCheckersAliveValue = new Text();
    private static final Text blackCheckersAliveValue = new Text();

    public static void printGameField(Stage stage) {
        Group root = new Group();
        Field field = Field.getInstance();
        CheckersLogic.setField(field);
        field.setGroup(root);
        field.setFriendCheckers(CheckersLogic.getFriendCheckers());
        field.setEnemyCheckers(CheckersLogic.getEnemyCheckers());
        field.setGroup(root);

        field.printField();
        field.printInitCheckers();

        Rectangle menu = new Rectangle();
        menu.setFill(Color.rgb(46, 20, 12));
        menu.setWidth(200);
        menu.setHeight(SCREEN_RESOLUTION_Y);
        menu.setX(SCREEN_RESOLUTION_X);
        menu.setY(0);

        countStepsValue.setFill(Color.rgb(158, 114, 101));
        currentStepValue.setFill(Color.rgb(158, 114, 101));
        whiteCheckersAliveValue.setFill(Color.rgb(158, 114, 101));
        blackCheckersAliveValue.setFill(Color.rgb(158, 114, 101));
        whiteCheckersAliveValue.setText("12");
        blackCheckersAliveValue.setText("12");
        currentStepValue.setText("White");
        countStepsValue.setText("0");
        whiteCheckersAliveValue.setFont(Font.font(25));
        blackCheckersAliveValue.setFont(Font.font(25));
        currentStepValue.setFont(Font.font(25));
        countStepsValue.setFont(Font.font(25));

        countStepsValue.setX(SCREEN_RESOLUTION_X + 15);
        countStepsValue.setY(105);
        currentStepValue.setX(SCREEN_RESOLUTION_X + 15);
        currentStepValue.setY(165);
        whiteCheckersAliveValue.setX(SCREEN_RESOLUTION_X + 15);
        whiteCheckersAliveValue.setY(225);
        blackCheckersAliveValue.setX(SCREEN_RESOLUTION_X + 15);
        blackCheckersAliveValue.setY(285);

        Rectangle giveUpButton = new Rectangle();
        giveUpButton.setHeight(80);
        giveUpButton.setWidth(150);
        giveUpButton.setFill(Color.rgb(52, 38, 33));
        giveUpButton.setArcWidth(25);
        giveUpButton.setArcHeight(25);
        giveUpButton.setX(SCREEN_RESOLUTION_X + 22);
        giveUpButton.setY(300);


        Text giveUpText = new Text();
        giveUpText.setText("Give Up");
        giveUpText.setFill((Color.rgb(158, 114, 101)));
        giveUpText.setFont(Font.font(25));
        giveUpText.setX(SCREEN_RESOLUTION_X + 45);
        giveUpText.setY(350);

        EventHandler<MouseEvent> eventHandler = e -> GiveUpPage.printGiveUpPage(stage);
        giveUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        giveUpText.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        Text countSteps = new Text();
        Text currentStep = new Text();
        Text whiteCheckersAlive = new Text();
        Text blackCheckersAlive = new Text();
        countSteps.setFont(Font.font(25));
        currentStep.setFont(Font.font(25));
        whiteCheckersAlive.setFont(Font.font(25));
        blackCheckersAlive.setFont(Font.font(25));
        countSteps.setFill(Color.rgb(158, 114, 101));
        currentStep.setFill(Color.rgb(158, 114, 101));
        whiteCheckersAlive.setFill(Color.rgb(158, 114, 101));
        blackCheckersAlive.setFill(Color.rgb(158, 114, 101));
        countSteps.setText("Count Steps:");
        countSteps.setX(SCREEN_RESOLUTION_X + 15);
        countSteps.setY(75);
        currentStep.setText("Current Step:");
        currentStep.setX(SCREEN_RESOLUTION_X + 15);
        currentStep.setY(135);
        whiteCheckersAlive.setText("White Alive:");
        whiteCheckersAlive.setX(SCREEN_RESOLUTION_X + 15);
        whiteCheckersAlive.setY(195);
        blackCheckersAlive.setText("Black Alive:");
        blackCheckersAlive.setX(SCREEN_RESOLUTION_X + 15);
        blackCheckersAlive.setY(255);

        root.getChildren().add(menu);
        root.getChildren().add(countSteps);
        root.getChildren().add(currentStep);
        root.getChildren().add(whiteCheckersAlive);
        root.getChildren().add(blackCheckersAlive);
        root.getChildren().add(countStepsValue);
        root.getChildren().add(currentStepValue);
        root.getChildren().add(whiteCheckersAliveValue);
        root.getChildren().add(blackCheckersAliveValue);
        root.getChildren().add(giveUpButton);
        root.getChildren().add(giveUpText);
        Scene scene = new Scene(root, SCREEN_RESOLUTION_X + 200, SCREEN_RESOLUTION_Y, Settings.FIELD_COLOR_ONE);
        stage.setScene(scene);
        stage.setTitle(GAME_NAME);
        stage.setResizable(false);
        stage.show();
    }


    public static void updateCountSteps() {
        int currentValue = Integer.parseInt(GameFieldPage.countStepsValue.getText());
        currentValue++;
        GameFieldPage.countStepsValue.setText(String.valueOf(currentValue));
    }

    public static void updateCurrentStep() {
        if (GameFieldPage.currentStepValue.getText().equals("White"))
            GameFieldPage.currentStepValue.setText("Black");
        else
            GameFieldPage.currentStepValue.setText("White");
    }

    public static void updateWhiteCheckersAlive() {
        int currentWhite = Integer.parseInt(GameFieldPage.whiteCheckersAliveValue.getText());
        currentWhite--;
        GameFieldPage.whiteCheckersAliveValue.setText(String.valueOf(currentWhite));
    }

    public static void updateBlackCheckersAlive() {
        try {
            int currentBlack = Integer.parseInt(GameFieldPage.blackCheckersAliveValue.getText());
            currentBlack--;
            GameFieldPage.blackCheckersAliveValue.setText(String.valueOf(currentBlack));
        } catch (NumberFormatException ignore) {
            // it doesn't work if we are testing logic
        }
    }

    public static Text getCurrentStepValue() {
        return currentStepValue;
    }

}
