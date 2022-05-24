package Dinosaur.Game;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Dino {
    private Group root; //контейнер, в котором хранятся
    private ImageView imageView;
    private int width = 50;//ширина
    private int height = 50;//высота
    private  int posX = 0;
    private int posY = 0;
    private int health = 3;

    public Dino(Group root, int width, int height) throws FileNotFoundException {
        this.root = root;
        this.width = width;
        this.height = height;
        Image image;


        FileInputStream fileInputStream = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/programming-task3-2022/app/src/main/resources/Image/charmander.png");
        FileInputStream fileInputStream2 = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/programming-task3-2022/app/src/main/resources/Image/picachy.png");
        FileInputStream fileInputStream3 = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/programming-task3-2022/app/src/main/resources/Image/squirtl.png");

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
        imageView.setFitHeight(this.height);
        imageView.setFitWidth(this.width);//? почему this
    }

    public Dino(){

    }
    public Group getRoot() {
        return root;
    }

    public int getWidth() {
        return width;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getHeight() {
        return height;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getHealth() {
        return health;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosX(int posX) {
        this.posX = posX;
        imageView.setTranslateX(this.posX);
    }

    public void setPosY(int posY) {
        this.posY = posY;
        imageView.setTranslateY(this.posY);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void jump() { // прыжок
       posY = posY - 180;
    }
}