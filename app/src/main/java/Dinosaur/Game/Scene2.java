package Dinosaur.Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Scene2 extends Application {
    private Stage stage;
    private Scene scene;
    private Group root = new Group();
    private Dino dino;
    private double random;
    private int n = 100;
    private Cactus cactus;
    private int res ;
    private ArrayList<Cactus> cactuses = new ArrayList<Cactus>();
    private Clouds cloud;
    private ArrayList<Clouds> clouds = new ArrayList<Clouds>();
    private boolean flag = false;//для жизни
    private int score = 0;
    private Label scoreLabel = new Label("Score: " + score);
    private Label healthLabel;
    private int level = 2;
    private Label levelLabel = new Label("Lavel: " + (level-1));
    private Label gameOver = new Label();
    private int a = -1;//для жизни
    private int flag2;// для жизни
    private Button btn = new Button();

    public Scene2() throws FileNotFoundException {

    }

    public void start (Stage primaryStage) throws FileNotFoundException {
        this.stage = primaryStage;
        stage.setMinHeight(625);
        stage.setMinWidth(600);
        stage.setMaxHeight(625);
        stage.setMaxWidth(600);

        setBackGround();
        theEnd();
        createContent();

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

        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (dino.getHealth() != 0){
                    upDate();
                } else {
                    gameOver.setVisible(true);//видимая надпись
                }
                if (flag) {
                    if (a != flag2) {
                        dino.setHealth(dino.getHealth() - 1);
                    }
                    a = flag2;
                }
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

    public void createContent() throws FileNotFoundException {
        dino = new Dino (root, 70, 70);
        dino.setPosX(50);
        dino.setPosY(530);

        scoreLabel.setTranslateX(545);
        healthLabel = new Label("Health: " + dino.getHealth());
        healthLabel.setTranslateX(545);
        healthLabel.setTranslateY(15);
        levelLabel.setTranslateX(545);
        levelLabel.setTranslateY(30);

        root.getChildren().addAll(scoreLabel, healthLabel, levelLabel);

        for (int i = 0; i < n; i++) { //добавление облаков и кактусов
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

            cloud = new Clouds(root, 70, 50);
            random = Math.random();
            res = 50 + (int) (random * ((300 - 50) + 1)); //от 50 до 300
            if (i != 0) {
                cloud.setPosX(clouds.get(i - 1).getPosX() + 100 + res);
            } else {
                cloud.setPosX(150);
            }
            res = 20 + (int) (random * ((150 - 20) + 1));//от 20 до 150
            cloud.setPosY(150 + res);
            clouds.add(cloud);
        }
    }

    public void upDate(){
        flag = false;
        if (dino.getPosY() < 530) {//гравитация
            if (level > 3) {
                dino.setPosY(dino.getPosY() + 2);
            } else {
                dino.setPosY(dino.getPosY() + 1);
            }
        }

        scoreLabel.setText("Score : " + score);
        healthLabel.setText("Health : "+ dino.getHealth());
        levelLabel.setText("Level : " + (level-1));

        for (int i = 0; i < n; i++ ){
            cactuses.get(i).setPosX(cactuses.get(i).getPosX() - level);//увеличение скорости у кактусов
            clouds.get(i).setPosX(clouds.get(i).getPosX()-1);

            if (cactuses.get(i).getPosX() == dino.getPosX()) {
               score += 1;

                if (score % 10 == 0) {//каждые 10 очков - увеличивается скорость
                    level += 1;
                }
            }

            if (dino.getPosX() > cactuses.get(i).getPosX() - dino.getWidth() &&
                    dino.getPosX() < cactuses.get(i).getPosX() + cactuses.get(i).getWidth() &&
                    dino.getPosY() > cactuses.get(i).getPosY() - dino.getHeight() &&
                    dino.getPosY() < cactuses.get(i).getPosY() + cactuses.get(i).getHeight()) {
                flag = true;
                flag2 = i;
            }
        }
    }

    public void theEnd() throws FileNotFoundException { //конец игры
        gameOver.setVisible(false);//невидимая
        root.getChildren().add(gameOver);
        FileInputStream fileInputStream = new FileInputStream("/Users/vladalodocnikova/IdeaProjects/Runner/src/main/resources/image/gameOver.png");
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        gameOver.setTranslateY(200);
        gameOver.setTranslateX(200);
        gameOver.setGraphic(imageView);
        gameOver.toFront();
    }
}