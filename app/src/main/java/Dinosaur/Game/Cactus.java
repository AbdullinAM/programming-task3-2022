package Dinosaur.Game;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Cactus {
    private Group root;
    private ImageView imageView;
    private int width = 50;//ширина
    private int height = 50;//высота
    private int posX = 0;
    private int posY = 0;

    public Cactus (Group root, int width, int height) throws FileNotFoundException {
        this.root = root;
        this.width = width;
        this.height = height;
        Image image;

        FileInputStream fileInputStream = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/programming-task3-2022/app/src/main/resources/Image/cact.png");
        FileInputStream fileInputStream2 = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/programming-task3-2022/app/src/main/resources/Image/cact2.png");
        FileInputStream fileInputStream3 = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/programming-task3-2022/app/src/main/resources/Image/cact3.png");

        double random = Math.random();
        int res = 1 + (int)(random*((3-1)+1)); //формула от 1 до 3 выбирает результат

        if (res == 1) { //рандом героев
            image = new Image(fileInputStream);
        } else if ( res == 2) {
            image = new Image(fileInputStream2);
        } else {
            image = new Image(fileInputStream3);
        }

        imageView = new ImageView(image);
        root.getChildren().add(imageView);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
    }

    public Cactus(){

    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        imageView.setFitWidth(width);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        imageView.setFitHeight(height);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
        imageView.setTranslateX(this.posX);
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
        imageView.setTranslateY(this.posY);
    }
}
