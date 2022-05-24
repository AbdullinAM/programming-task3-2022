package Dinosaur.Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Scene2 extends Application {
    private Stage stage;
    Scene scene;
    private Group root = new Group();
    private Dino dino;
    private double random;
    private int n = 100;
    private Cactus cactus;
    //private int m = 100;
    private int res ;
    private ArrayList<Cactus> cactuses = new ArrayList<Cactus>();
    private Clouds cloud;
    private ArrayList<Clouds> clouds = new ArrayList<Clouds>();
    private boolean flag = false;
    private int score = 0;
    private Label scoreLabel = new Label("Score: " + score);
    private Label healthLabel;
    private Label endLabel = new Label();
    private int level = 2;
    private Label levelLabel = new Label("Lavel: " + (level-1));


    public Scene2() throws FileNotFoundException {

    }

    public void start (Stage primaryStage) throws FileNotFoundException {
        this.stage = primaryStage;

        //setBackGround();
        game();


        scene = new Scene(root, 600, 600, Color.WHITE);
        primaryStage.setTitle("Dino");

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (dino.getPosY() == 530) {
                    dino.jump();
                }
            }
        });

//        primaryStage.setMinWidth(600); //ограничение окна
//        primaryStage.setMinHeight(600);
//        primaryStage.setWidth(600);
//        primaryStage.setHeight(600);
//        primaryStage.setMaxWidth(600);
//        primaryStage.setMaxHeight(600);

        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                upDate();
            }
        };
        timer.start();

    }

    public void setBackGround() throws FileNotFoundException { //задний фон
        FileInputStream fileInputStream = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/programming-task3-2022/app/src/main/resources/Image/Picture.png");
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.toBack();
        imageView.setFitHeight(600);
        imageView.setFitWidth(600);
        root.getChildren().add(imageView);
    }

    public void game() throws FileNotFoundException {
        dino = new Dino (root, 70, 70);
        dino.setPosX(50);
        dino.setPosY(530);

        scoreLabel.setTranslateX(545);
        healthLabel = new Label("Health: " + dino.getHealth());
        healthLabel.setTranslateX(545);
        healthLabel.setTranslateY(15);
        levelLabel.setTranslateX(545);
        levelLabel.setTranslateY(30);

        root.getChildren().addAll(scoreLabel, healthLabel,levelLabel);

        for (int i = 0; i < n; i++) {
            cactus = new Cactus(root, 70,70);
            random = Math.random();
            res = 50 + (int) (random * ((600 - 50) + 1)); // от 50 до 600
            if (i != 0) {
                cactus.setPosX(cactuses.get(i - 1).getPosX() + 300 + res);
            } else {
                cactus.setPosX(600);//первое
            }
            cactus.setPosY(600 - cactus.getHeight());
            cactuses.add(cactus);

            cloud = new Clouds(root, 100, 200);
            random = Math.random();
            res = 50 + (int) (random * ((300 - 50) + 1));
            if (i != 0) {
                cloud.setPosX(clouds.get(i - 1).getPosX() + 100 + res);
            } else {
                cloud.setPosX(150);
            }
            res = 20 + (int) (random * ((150 - 20) + 1));
            cloud.setPosY(150 + res);
            clouds.add(cloud);
        }
    }

    public void upDate(){
        flag = false;
        if (dino.getPosY() < 530) {//гравитация
            dino.setPosY(dino.getPosY() + 1);
        }

        scoreLabel.setText("Score : " + score);
        healthLabel.setText("Health : "+ dino.getHealth());
        levelLabel.setText("Level : " + (level -1));


        for (int i = 0; i < n; i++ ){
            cactuses.get(i).setPosX(cactuses.get(i).getPosX() - level);
            clouds.get(i).setPosX(clouds.get(i).getPosX()-1);

            if (cactuses.get(i).getPosX() == dino.getPosX()) {
                score += 1;
                if (score % 5 == 0) { //каждые 15 очков - увеличивается скорость
                    level += 1;
                }
            }

        }
    }
}