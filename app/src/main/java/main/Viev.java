package main;

import core.Cell;
import core.Field;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Viev {
    //raw values and constants
    public final static int WINDOW_WIDTH = 800;
    public final static int WINDOW_HEIGHT = 600;
    private AnchorPane tileMap;
    private final Image imageBombExploded = new Image("image_bomb_exploded.png");
    private final ImagePattern patternBombExploded = new ImagePattern(imageBombExploded);
    private final Image imageBomb = new Image("image_bomb.png");
    private final ImagePattern patternBomb = new ImagePattern(imageBomb);
    private final Image imageFlag = new Image("image_flag.png");
    private final ImagePattern patternFlag = new ImagePattern(imageFlag);
    private final Image image_0 = new Image("image_0.png");
    private final ImagePattern patternImage_0 = new ImagePattern(image_0);
    private final Image image_1 = new Image("image_1.png");
    private final ImagePattern patternImage_1 = new ImagePattern(image_1);
    private final Image image_2 = new Image("image_2.png");
    private final ImagePattern patternImage_2 = new ImagePattern(image_2);
    private final Image image_3 = new Image("image_3.png");
    private final ImagePattern patternImage_3 = new ImagePattern(image_3);
    private final Image image_4 = new Image("image_4.png");
    private final ImagePattern patternImage_4 = new ImagePattern(image_4);
    private final Image image_5 = new Image("image_5.png");
    private final ImagePattern patternImage_5 = new ImagePattern(image_5);
    private final Image image_6 = new Image("image_6.png");
    private final ImagePattern patternImage_6 = new ImagePattern(image_6);
    private boolean defeat = true;
    private final Alert alertHelp = new Alert(Alert.AlertType.INFORMATION);
    private final Alert alertDefeat = new Alert(Alert.AlertType.INFORMATION);
    private final Alert alertWin = new Alert(Alert.AlertType.INFORMATION);
    private final AtomicInteger correctMarkMines = new AtomicInteger();
    private final AtomicInteger totalMarkMines = new AtomicInteger();
    private final Label amountLeftMines = new Label();
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
        }
        tileMap.getChildren().add(tile);
    }

    //draws cells around the given
    private void showAround(Field field, int col, int row, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        int x = field.getCol();
        int y = field.getRow();
        if (col - 1 >= 0) showAroundHelper( field,col - 1, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        if (col + 1 < x) showAroundHelper(field,col + 1, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        if (row - 1 >= 0) showAroundHelper(field,col, row - 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        if (row + 1 < y) showAroundHelper(field,col, row + 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        if (row % 2 == 0) {
            if (col - 1 >= 0 && row - 1 >= 0) showAroundHelper(field,col - 1, row - 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            if (col - 1 >= 0 && row + 1 < y) showAroundHelper(field,col - 1, row + 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        } else {
            if (col + 1 < x && row - 1 >= 0) showAroundHelper(field,col + 1, row - 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            if (col + 1 < x && row + 1 < y) showAroundHelper(field,col + 1, row + 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        }
    }

    //recursively opens cells around
    private void updateFieldAround(Field field, int col, int row, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        if (field.getMineAround(col, row) == 0) {
            field.openAroundCell(col, row);
            showAround(field, col, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            int x = field.getCol();
            int y = field.getRow();
            boolean correctColLeft = col > 0;
            boolean correctColRight = col + 1 < x;
            boolean correctRowLeft = row > 0;
            boolean correctRowRight = row + 1 < y;
            if (correctColLeft && field.getMineAround(col - 1, row) == 0 && field.hasHiddenAround(col - 1, row))
                updateFieldAround(field, col - 1, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            if (correctColRight && field.getMineAround(col + 1, row) == 0 && field.hasHiddenAround(col + 1, row))
                updateFieldAround(field, col + 1, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            if (correctRowRight && field.getMineAround(col, row + 1) == 0 && field.hasHiddenAround(col, row + 1))
                updateFieldAround(field, col, row + 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            if (correctRowLeft && field.getMineAround(col, row - 1) == 0 && field.hasHiddenAround(col, row - 1))
                updateFieldAround(field, col, row - 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            if (row % 2 == 0) {
                if (correctRowLeft && correctColLeft && field.getMineAround(col - 1, row - 1) == 0 && field.hasHiddenAround(col - 1, row - 1))
                    updateFieldAround(field, col - 1, row - 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
                if (correctColLeft && correctRowRight && field.getMineAround(col - 1, row + 1) == 0 && field.hasHiddenAround(col - 1, row + 1))
                    updateFieldAround(field, col - 1, row + 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            } else {
                if (correctColRight && correctRowLeft && field.getMineAround(col + 1, row - 1) == 0 && field.hasHiddenAround(col + 1, row - 1))
                    updateFieldAround(field, col + 1, row - 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
                if (correctColRight && correctRowRight && field.getMineAround(col + 1, row + 1) == 0 && field.hasHiddenAround(col + 1, row + 1))
                    updateFieldAround(field, col + 1, row + 1, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
            }
        }
    }

    //draws the field and responds to interaction with it
    private void updateField(Field field, int col, int row, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        correctMarkMines.set(0);
        totalMarkMines.set(0);
        int amountMines = field.getAmountMine();
        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                double xCord = x * TILE_WIDTH + (y % 2) * n + shiftX;
                double yCord = y * TILE_HEIGHT * 0.75 + shiftY;
                Polygon tile = new Tile(xCord, yCord, TILE_WIDTH, n, r);
                tileMap.getChildren().addAll(tile);
                int finalX = x;
                int finalY = y;

                tile.setOnMouseClicked(event -> {
                    Cell cell = field.getCell(finalX, finalY);
                    Cell.ClickResult clickResult = cell.clickResult(event.getButton());

                    switch (clickResult) {
                        case Default -> {
                            if (cell.getState() != Cell.State.MineExploded && cell.isHidden() && event.getButton().equals(MouseButton.SECONDARY)) {
                                if (cell.isMark()) {
                                    tile.setFill(patternFlag);
                                    totalMarkMines.getAndIncrement();
                                    if (cell.getState() == Cell.State.Mine) correctMarkMines.getAndIncrement();
                                } else {
                                    tile.setFill(Color.ANTIQUEWHITE);
                                    totalMarkMines.getAndDecrement();
                                    if (cell.getState() == Cell.State.Mine) correctMarkMines.getAndDecrement();
                                }
                                int leftMines = amountMines - totalMarkMines.get();
                                if (leftMines >= 0) amountLeftMines.setText(Integer.toString(leftMines));
                                else amountLeftMines.setText("ERR");
                            }

                            if (correctMarkMines.get() == (amountMines) && correctMarkMines.get() == totalMarkMines.get()) defeat = false;
                            if (correctMarkMines.get() != (amountMines) || correctMarkMines.get() != totalMarkMines.get()) defeat = true;
                        }

                        case Explode -> {
                            defeat = true;
                            tile.setFill(patternBombExploded);
                            amountLeftMines.setText("FAIL");
                            alertDefeat.showAndWait();
                            showAll(field, col, row, defeat, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
                        }

                        case Open -> {
                            fillingTile(field, tile, finalX, finalY);
                            if (cell.getState() == Cell.State.Empty && !cell.isMark() && field.getMineAround(finalX, finalY) == 0) {
                                updateFieldAround(field, finalX, finalY, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
                            }
                            if (amountMines == 0) {
                                defeat = false;
                            }
                        }
                    }
                });
            }
        }
    }

    //draws the entire field at the end of the game
    private void showAll(Field field,int col, int row, boolean defeat, double TILE_WIDTH, double TILE_HEIGHT, double shiftX, double shiftY, double n, double r) {
        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                double xCord = x * TILE_WIDTH + (y % 2) * n + shiftX;
                double yCord = y * TILE_HEIGHT * 0.75 + shiftY;
                Polygon tile = new Tile(xCord, yCord, TILE_WIDTH, n, r);
                Cell cell = field.getCell(x, y);
                switch (cell.getState()) {
                    case Empty -> tile.setFill(patternImage_0);
                    default -> {
                        if (defeat) tile.setFill(patternBombExploded);
                        else tile.setFill(patternBomb);
                    }
                }
                tileMap.getChildren().add(tile);
            }
        }
    }

    //main method, set scene
    public void start(Stage primaryStage, int col, int row, int amountMines) {
        alertHelp.setTitle("Help");
        alertHelp.setContentText("So, on LMB you open a cell, on RMB - put or remove the flag. " + System.lineSeparator() +
                "Your task is to mark all the cells in which the mine is located, while not bumping into it. " + System.lineSeparator() +
                "As soon as you think that you have coped with the task," + System.lineSeparator() +
                "you need to confirm your choice and check the correctness of your guesses.");
        alertHelp.setHeaderText("Help");
        alertDefeat.setTitle("End Game");
        alertDefeat.setHeaderText("Defeat");
        alertDefeat.setContentText("Oops, it looks like you've failed" + System.lineSeparator() + "You can RESTART the game(press \"R\") or EXIT(press \"Esc\")");
        alertWin.setTitle("End Game");
        alertWin.setHeaderText("WIN!!!"+ System.lineSeparator() + "You can RESTART the game(press \"R\") or EXIT(press \"Esc\")");
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
            if (defeat) {
                amountLeftMines.setText("FAIL");
                alertDefeat.showAndWait();
            }
            else {
                amountLeftMines.setText("WIN");
                alertWin.showAndWait();
            }
            showAll(field.get(), col, row, defeat, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
        });
        close.setOnAction(event -> primaryStage.close());
        help.setOnAction(event -> alertHelp.showAndWait());
        updateField(field.get(), col, row, TILE_WIDTH, TILE_HEIGHT, shiftX, shiftY, n, r);
    }
}