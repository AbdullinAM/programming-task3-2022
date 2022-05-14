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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.russianCheckers.ui.Settings.*;

@Data
public class FieldUI implements Runnable {

    private FieldUI() {}

    private Stage stage;
    private Group root = new Group();
    private volatile boolean isNeedInterrupt = false;

    private static FieldUI fieldUI = new FieldUI();
    public static FieldUI getFieldUI() {
        return fieldUI;
    }

    private Map<Checker, Circle> checkerUI = new HashMap<>();
    private Map<String, Rectangle> cellUI = new LinkedHashMap<>();
    public void printGameField() {
        root = new Group();
        printCells(root);
        printCheckers(root);

        Rectangle menu = new Rectangle();
        menu.setFill(Color.rgb(46, 20, 12));
        menu.setWidth(200);
        menu.setHeight(SCREEN_RESOLUTION_Y);
        menu.setX(SCREEN_RESOLUTION_X);
        menu.setY(0);


        Rectangle refreshButton = new Rectangle();
        refreshButton.setHeight(80);
        refreshButton.setWidth(150);
        refreshButton.setFill(Color.rgb(52, 38, 33));
        refreshButton.setArcWidth(25);
        refreshButton.setArcHeight(25);
        refreshButton.setX(SCREEN_RESOLUTION_X + 22);
        refreshButton.setY(25);

        Text refreshText = new Text();
        refreshText.setText("Refresh");
        refreshText.setFill((Color.rgb(158, 114, 101)));
        refreshText.setFont(Font.font(25));
        refreshText.setX(SCREEN_RESOLUTION_X + 45);
        refreshText.setY(75);

        EventHandler<MouseEvent> eventHandler = e -> {
            FieldModifier.restartGame();
            this.printGameField();
            isNeedInterrupt = false;
        };
        refreshButton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        refreshText.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        root.getChildren().add(menu);
        root.getChildren().add(refreshButton);
        root.getChildren().add(refreshText);
        Scene scene = new Scene(root, SCREEN_RESOLUTION_X + 200, SCREEN_RESOLUTION_Y, Settings.FIELD_COLOR_ONE);
        stage.setScene(scene);
        stage.setTitle(GAME_NAME);
        stage.setResizable(false);
        stage.show();
    }

    private void printCells(Group group) {
        cellUI.clear();
        boolean whiteCell = true;
        for (int x = 0; x <= SCREEN_RESOLUTION_X; x += CELL_LENGTH) {
            for (int y = 0; y <= SCREEN_RESOLUTION_Y; y += CELL_LENGTH) {
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(CELL_LENGTH);
                rectangle.setWidth(CELL_LENGTH);
                rectangle.setX(x);
                rectangle.setY(y);
                rectangle.setViewOrder(100);

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
        checkerUI.clear();
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
            if (isNeedInterrupt) continue;
            else {
                try {
                    Thread.sleep(25);
                    // render available positions
                    List<String> steps = FieldModifier.getAvailableSteps();
                    for (Map.Entry<String, Rectangle> cell : cellUI.entrySet()) {
                        if (steps != null) {
                            if (steps.stream().anyMatch(step -> step.equals(cell.getKey())))
                                cell.getValue().setFill(ACTIVE_COLOR_CELL);
                            else if (cell.getValue().getFill() != FIELD_COLOR_ONE)
                                cell.getValue().setFill(FIELD_COLOR_TWO);
                        }
                    }

                    // render checkers
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
    }

    public void renderEndGamePage() {
        isNeedInterrupt = true;
        Text text = new Text();
        root = new Group();
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
        welcomeButton.setY(SCREEN_RESOLUTION_Y / 2 - 60);

        Text welcomeText = new Text();
        welcomeText.setText("Welcome Page");
        welcomeText.setFont(Font.font(30));
        welcomeText.setFill(Color.rgb(158, 114, 101));
        welcomeText.setX(SCREEN_RESOLUTION_X / 2 - 10);
        welcomeText.setY(SCREEN_RESOLUTION_Y / 2);


        Rectangle textBackGround = new Rectangle();
        textBackGround.setX(SCREEN_RESOLUTION_X / 2 - 200);
        textBackGround.setY(SCREEN_RESOLUTION_Y / 2 - 150);
        textBackGround.setWidth(600);
        textBackGround.setHeight(300);
        textBackGround.setArcWidth(30);
        textBackGround.setArcHeight(30);
        textBackGround.setFill(Color.rgb(52, 38, 33));

        EventHandler<MouseEvent> eventHandler = e -> {
            FieldModifier.restartGame();
            this.printGameField();
            isNeedInterrupt = false;
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

    public void printWelcomePage() {
        root = new Group();
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

        EventHandler<MouseEvent> eventHandler = e -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(fieldUI);
            fieldUI.printGameField();
        };
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

    public Map<Checker, Circle> getCheckerUI() {
        return this.checkerUI;
    }

    @Override
    public void run() {
        refreshFieldUI();
    }

}
