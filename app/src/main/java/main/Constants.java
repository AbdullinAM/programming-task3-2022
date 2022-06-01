package main;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Constants {
    public final static int WINDOW_WIDTH = 800;
    public final static int WINDOW_HEIGHT = 600;

    public static final Alert alertHelp = new Alert(Alert.AlertType.INFORMATION);
    public static final Alert alertDefeat = new Alert(Alert.AlertType.INFORMATION);
    public static final Alert alertWin = new Alert(Alert.AlertType.INFORMATION);
    public static final Label amountLeftMines = new Label();

    public final static String helpText = "So, on LMB you open a cell, on RMB - put or remove the flag. " +
            System.lineSeparator() + "Your task is to mark all the cells in which the mine is located, " +
            "while not bumping into it. " + System.lineSeparator() + "As soon as you think that you have coped with the task," +
            System.lineSeparator() + "you need to confirm your choice and check the correctness of your guesses.";

    public final static String defeatText = "Oops, it looks like you've failed" + System.lineSeparator() +
            "You can RESTART the game(press \"R\") or EXIT(press \"Esc\")";

    public final static String winText = "WIN!!!"+ System.lineSeparator() +
            "You can RESTART the game(press \"R\") or EXIT(press \"Esc\")";

    public static final Image imageBombExploded = new Image("image_bomb_exploded.png");
    public static final ImagePattern patternBombExploded = new ImagePattern(imageBombExploded);
    public static final Image imageBomb = new Image("image_bomb.png");
    public static final ImagePattern patternBomb = new ImagePattern(imageBomb);
    public static final Image imageFlag = new Image("image_flag.png");
    public static final ImagePattern patternFlag = new ImagePattern(imageFlag);
    public static final Image image_0 = new Image("image_0.png");
    public static final ImagePattern patternImage_0 = new ImagePattern(image_0);
    public static final Image image_1 = new Image("image_1.png");
    public static final ImagePattern patternImage_1 = new ImagePattern(image_1);
    public static final Image image_2 = new Image("image_2.png");
    public static final ImagePattern patternImage_2 = new ImagePattern(image_2);
    public static final Image image_3 = new Image("image_3.png");
    public static final ImagePattern patternImage_3 = new ImagePattern(image_3);
    public static final Image image_4 = new Image("image_4.png");
    public static final ImagePattern patternImage_4 = new ImagePattern(image_4);
    public static final Image image_5 = new Image("image_5.png");
    public static final ImagePattern patternImage_5 = new ImagePattern(image_5);
    public static final Image image_6 = new Image("image_6.png");
    public static final ImagePattern patternImage_6 = new ImagePattern(image_6);
}
