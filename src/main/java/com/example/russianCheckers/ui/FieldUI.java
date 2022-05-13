package com.example.russianCheckers.ui;

import com.example.russianCheckers.logic.Checker;
import com.example.russianCheckers.logic.CheckerStatus;
import com.example.russianCheckers.logic.Field;
import com.example.russianCheckers.logic.FieldModifier;
import com.example.russianCheckers.logic.listeners.Listener;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;

import java.util.*;

import static com.example.russianCheckers.ui.Settings.*;
import static java.lang.Math.*;

@Data
public class FieldUI implements Runnable {

    private FieldUI() {}

    private Stage stage;

    private static FieldUI fieldUI = new FieldUI();
    public static FieldUI getFieldUI() {
        return fieldUI;
    }

    private Map<Checker, Circle> checkerUI = new HashMap<>();
    private Map<String, Rectangle> cellUI = new LinkedHashMap<>();
    public void printGameField() {
        Group root = new Group();

        printCells(root);
        printCheckers(root);

        Rectangle menu = new Rectangle();
        menu.setFill(Color.rgb(46, 20, 12));
        menu.setWidth(200);
        menu.setHeight(SCREEN_RESOLUTION_Y);
        menu.setX(SCREEN_RESOLUTION_X);
        menu.setY(0);


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
        root.getChildren().add(giveUpButton);
        root.getChildren().add(giveUpText);
        Scene scene = new Scene(root, SCREEN_RESOLUTION_X + 200, SCREEN_RESOLUTION_Y, Settings.FIELD_COLOR_ONE);
        stage.setScene(scene);
        stage.setTitle(GAME_NAME);
        stage.setResizable(false);
        stage.show();
    }

    private void printCells(Group group) {
        boolean whiteCell = true;
        for (int x = 0; x <= SCREEN_RESOLUTION_X; x += CELL_LENGTH) {
            for (int y = 0; y <= SCREEN_RESOLUTION_Y; y += CELL_LENGTH) {
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(CELL_LENGTH);
                rectangle.setWidth(CELL_LENGTH);
                rectangle.setX(x);
                rectangle.setY(y);

                int selectedX = y / CELL_LENGTH + 1;
                int selectedY = x / CELL_LENGTH + 1;
                char posX = (char) (selectedX + 64);
                String position = posX + String.valueOf(selectedY);

                rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, Listener.cellListener());

                cellUI.put(position, rectangle);
                if (whiteCell) rectangle.setFill(FIELD_COLOR_ONE);
                else rectangle.setFill(FIELD_COLOR_TWO);
                group.getChildren().add(rectangle);
                whiteCell = !whiteCell;
            }
        }
    }

    private void printCheckers(Group group) {
        for (Map.Entry<String, Checker> checkerEntry: Field.getCells().entrySet()) {
            Circle circle = new Circle();
            circle.setRadius(35);
            circle.setCenterX(FieldModifier.getXFromPosition(checkerEntry.getKey()) - CELL_LENGTH / 2);
            circle.setCenterY(FieldModifier.getYFromPosition(checkerEntry.getKey()) - CELL_LENGTH / 2);
            circle.setViewOrder(-100);
            if (checkerEntry.getValue().getCheckerStatus() == CheckerStatus.WHITE) {
                circle.setFill(WHITE_CHECKER);
                circle.addEventHandler(MouseEvent.MOUSE_CLICKED, Listener.checkerListener());
                checkerUI.put(checkerEntry.getValue(), circle);
                group.getChildren().add(circle);
            }
            if (checkerEntry.getValue().getCheckerStatus() == CheckerStatus.BLACK) {
                circle.setFill(BLACK_CHECKER);
                circle.addEventHandler(MouseEvent.MOUSE_CLICKED, Listener.checkerListener());
                checkerUI.put(checkerEntry.getValue(), circle);
                group.getChildren().add(circle);
            }
        }
    }

    public void refreshFieldUI() {
        while (true) {
            try {
                Thread.sleep(25);

                // render available positions
                List<String> steps = FieldModifier.getAvailableSteps();
                for (Map.Entry<String, Rectangle> cell : cellUI.entrySet()) {
                    if (steps != null) {
                        if (steps.stream().anyMatch(step -> step.equals(cell.getKey()))) cell.getValue().setFill(ACTIVE_COLOR_CELL);
                        else if (cell.getValue().getFill() != FIELD_COLOR_ONE) cell.getValue().setFill(FIELD_COLOR_TWO);
                    }
                }

                // render translation checkers
                checkerUI.entrySet().stream().filter(checker -> !checker.getKey().getPosition().equals(FieldModifier.coordinatesToPosition(checker.getValue().getCenterX() + ((double) CELL_LENGTH) / 2, checker.getValue().getCenterY() + ((double) CELL_LENGTH) / 2))).forEach(checker -> {
                    checker.getValue().setCenterX(FieldModifier.getXFromPosition(checker.getKey().getPosition()) - ((double) CELL_LENGTH) / 2);
                    checker.getValue().setCenterY(FieldModifier.getYFromPosition(checker.getKey().getPosition()) - ((double) CELL_LENGTH) / 2);
                });

                // render ate checkers
                checkerUI.entrySet().stream().filter(checker -> checker.getKey().getCheckerStatus() == CheckerStatus.FREE).forEach(checkerUI -> checkerUI.getValue().setRadius(0));

                // render superChecker
                checkerUI.entrySet().stream().filter(checker -> checker.getKey().isSuperChecker()).filter(checker -> checker.getKey().getCheckerStatus() == CheckerStatus.WHITE).forEach(checker -> checker.getValue().setFill(SUPER_CHECKER_COLOR_WHITE));
                checkerUI.entrySet().stream().filter(checker -> checker.getKey().isSuperChecker()).filter(checker -> checker.getKey().getCheckerStatus() == CheckerStatus.BLACK).forEach(checker -> checker.getValue().setFill(SUPER_CHECKER_COLOR_BLACK));
            } catch (Exception ignore) {}
        }
    }

    public Map<Checker, Circle> getCheckerUI() {
        return this.checkerUI;
    }

    @Override
    public void run() {
        refreshFieldUI();
    }

}
