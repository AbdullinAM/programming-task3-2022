package main;

import core.Cell;
import core.Field;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static main.Constants.*;

public class UI {
    private AnchorPane tileMap;

    //setting the value of the radius of the hexagon depending on their number
    private double setRadius(int x, int y) {
        double r = 10;
        if (x <= 10 && y <= 10) r = 30;
        else if (x <= 20 && y <= 20) r = 15;
        else if (x <= 50 && y<= 50) r = 7;
        return r;
    }

    //default hexagon
    private static class Tile extends Polygon {
        Tile(double x, double y, double TILE_WIDTH, double n, double r) {
            getPoints().addAll(
                    x, y,
                    x, y + r,
                    x + n, y + r * 1.5,
                    x + TILE_WIDTH, y + r,
                    x + TILE_WIDTH, y,
                    x + n, y - r * 0.5
            );
            setFill(Color.ANTIQUEWHITE);
            setStrokeWidth(1);
            setStroke(Color.BLACK);
        }
    }

    //cell filling
    private void fillingTile(Field field, Polygon tile, int x, int y) {
        int mineAround = field.getMineAround(x, y);
        if (mineAround == 0) tile.setFill(patternImage_0);
        if (mineAround == 1) tile.setFill(patternImage_1);
        if (mineAround == 2) tile.setFill(patternImage_2);
        if (mineAround == 3) tile.setFill(patternImage_3);
        if (mineAround == 4) tile.setFill(patternImage_4);
        if (mineAround == 5) tile.setFill(patternImage_5);
        if (mineAround == 6) tile.setFill(patternImage_6);
    }

    //draws the given cell
    private void showAroundHelper(Field field, int x, int y, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        double xCord = x * TILE_WIDTH + (y % 2) * n + shiftX;
        double yCord = y * TILE_HEIGHT * 0.75 + shiftY;
        Polygon tile = new Tile(xCord, yCord, TILE_WIDTH, n, r);
        Cell cell = field.getCell(x, y);
        if (cell.getState() != Cell.State.Mine) {
            fillingTile(field, tile, x, y);
            cell.setMark(false);
        }
        tileMap.getChildren().add(tile);
    }

    //draws cells around the given
    private void showAround(Field field, int col, int row, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        Map<Integer, Pair<Integer, Integer>> neighbours = field.getNeighbours(col, row);
        for (int i = 0; i < 6; i++) {
            Pair<Integer, Integer> cords = neighbours.get(i);
            if (cords != null) showAroundHelper(field, cords.getKey(), cords.getValue(), TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        }
    }

    //recursively opens cells around
    private void updateFieldAround(Field field, int col, int row, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        if (field.getMineAround(col, row) == 0) {
            field.openAroundCell(col, row);
            showAround(field, col, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            Map<Integer, Pair<Integer, Integer>> neighbours = field.getNeighbours(col, row);
            for (int i = 0; i < 6; i++) {
                Pair<Integer, Integer> cords = neighbours.get(i);
                if (cords != null) {
                    int x = cords.getKey();
                    int y = cords.getValue();
                    if (field.getMineAround(x, y) == 0 && field.hasHiddenAround(x, y)) {
                        updateFieldAround(field, x, y, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
                    }
                }
            }
        }
    }

    //draws the field and responds to interaction with it
    private void updateField(Field field, int col, int row, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                double xCord = x * TILE_WIDTH + (y % 2) * n + shiftX;
                double yCord = y * TILE_HEIGHT * 0.75 + shiftY;
                Polygon tile = new Tile(xCord, yCord, TILE_WIDTH, n, r);
                tileMap.getChildren().add(tile);
                int finalX = x;
                int finalY = y;

                tile.setOnMouseClicked(event -> {
                    Cell cell = field.getCell(finalX, finalY);
                    Cell.ClickResult clickResult = cell.clickResult(event.getButton().toString());
                    Cell.State state = cell.getState();

                    switch (clickResult) {
                        case Default -> {
                            if (state != Cell.State.MineExploded && cell.isHidden() && event.getButton().equals(MouseButton.SECONDARY)) {
                                if (cell.isMark()) {
                                    tile.setFill(patternFlag);
                                } else {
                                    tile.setFill(Color.ANTIQUEWHITE);
                                }
                                remainingBombsCounter(field);
                            }
                        }

                        case Explode -> {
                            tile.setFill(patternBombExploded);
                            amountLeftMines.setText("FAIL");
                            alertDefeat.showAndWait();
                            showAll(field, col, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
                        }

                        case Open -> {
                            fillingTile(field, tile, finalX, finalY);
                            if (state == Cell.State.Empty && !cell.isMark() && field.getMineAround(finalX, finalY) == 0) {
                                updateFieldAround(field, finalX, finalY, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
                            }
                            remainingBombsCounter(field);
                        }
                    }
                });
            }
        }
    }

    //draws the entire field at the end of the game
    private void showAll(Field field,int col, int row, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                double xCord = x * TILE_WIDTH + (y % 2) * n + shiftX;
                double yCord = y * TILE_HEIGHT * 0.75 + shiftY;
                Polygon tile = new Tile(xCord, yCord, TILE_WIDTH, n, r);
                Cell cell = field.getCell(x, y);
                if (cell.getState() == Cell.State.Empty) {
                    fillingTile(field, tile, x, y);
                } else {
                    if (field.checkGameOver()) tile.setFill(patternBombExploded);
                    else tile.setFill(patternBomb);
                }
                tileMap.getChildren().add(tile);
            }
        }
    }

    private void remainingBombsCounter(Field field) {
        int leftMines = field.getAmountMine() - field.getTotalMarkMines();
        if (leftMines >= 0) amountLeftMines.setText(Integer.toString(leftMines));
        else amountLeftMines.setText("ERR");
    }

    //main method, set scene
    public void start(Stage primaryStage, int col, int row, int amountMines) {
        alertHelp.setTitle("Help");
        alertHelp.setContentText(helpText);
        alertHelp.setHeaderText("Help");
        alertDefeat.setTitle("End Game");
        alertDefeat.setHeaderText("Defeat");
        alertDefeat.setContentText(defeatText);
        alertWin.setTitle("End Game");
        alertWin.setHeaderText(winText);
        alertWin.setContentText("Your strategic skills are amazing, congratulations, you won");

        double r = setRadius(col, row);
        int shiftX = 2 * (int) r;
        int shiftY = 2 * (int) r;
        double n = Math.sqrt(r * r * 0.75);
        double TILE_HEIGHT = 2 * r;
        double TILE_WIDTH = 2 * n;

        AtomicReference<Field> field = new AtomicReference<>(new Field(col, row, amountMines));
        VBox root = new VBox();
        tileMap = new AnchorPane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        updateField(field.get(), col, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);

        Button restart = new Button("Restart");
        Button confirm = new Button("Confirm");
        Button close = new Button("Exit");
        Button help = new Button("Help");

        Text settings = new Text("Field size: "  + col + "x" + row + "; Mines: " + amountMines + System.lineSeparator() + "Press Q to Confirm; R to Restart; H for Help");
        settings.setFill(Color.BROWN);
        settings.setStyle("-fx-font: 22 arial");

        HBox leftMines = new HBox();
        Text textLeftMines = new Text("LEFT: ");
        textLeftMines.setFill(Color.RED);
        amountLeftMines.setText(Integer.toString(amountMines));
        amountLeftMines.setTextFill(Color.RED);
        leftMines.setStyle("-fx-font: 22 arial");
        leftMines.setPadding(new Insets(0, 0, 0, 310));
        leftMines.getChildren().addAll(textLeftMines, amountLeftMines);

        HBox menu = new HBox();
        FlowPane toolBar = new FlowPane();
        menu.setSpacing(30);
        menu.getChildren().addAll(confirm, restart, help, close, settings);
        toolBar.getChildren().addAll(menu, leftMines);

        root.getChildren().addAll(toolBar, tileMap);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.R)) {
                restart.fire();
            }
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                close.fire();
            }
            if (event.getCode().equals(KeyCode.Q)) {
                confirm.fire();
            }
            if (event.getCode().equals(KeyCode.H)) {
                help.fire();
            }
        });

        restart.setOnAction(e -> {
            field.set(new Field(col, row, amountMines));
            updateField(field.get(), col, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            amountLeftMines.setText(Integer.toString(amountMines));
        });

        confirm.setOnAction(event -> {
            if (field.get().checkGameOver()) {
                amountLeftMines.setText("FAIL");
                alertDefeat.showAndWait();
            }
            else {
                amountLeftMines.setText("WIN");
                alertWin.showAndWait();
            }
            showAll(field.get(), col, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        });
        close.setOnAction(event -> primaryStage.close());
        help.setOnAction(event -> alertHelp.showAndWait());
    }
}