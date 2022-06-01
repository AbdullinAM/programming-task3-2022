package programming.task3;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    public static Pane appPane = new Pane();
    public static Pane gamePane = new Pane();
    public static Pane backgroundPane = new Pane();
    private final int height = 600;
    private final int width = 600;

    public static ArrayList<Obstacle> obstacles = new ArrayList<>();
    private UserController userController = new UserController();
    private Bird bird = new Bird();
    public static int score = 0;
    private Label scoreLabel = new Label("Score: " + score);

    private Parent windowController() {
        gamePane.setPrefSize(width, height);
        generateMap();
        backgroundPane.getChildren().add(new Background());
        gamePane.getChildren().add(bird);
        appPane.getChildren().addAll(backgroundPane);
        appPane.getChildren().addAll(gamePane, scoreLabel);
        return appPane;
    }

    private void generateMap(){

        for (int i = 0; i < 100; i++) {
            int hole = (int)((Math.random() * (150 - 140)) + 140);
            int pipeHeight = new Random().nextInt(600 - hole);
            Obstacle obstacleDown = new Obstacle(pipeHeight);
            obstacleDown.setTranslateX(i * 400 + width);
            obstacleDown.setTranslateY(0);
            obstacles.add(obstacleDown);

            Obstacle obstacleUp = new Obstacle(height - hole - pipeHeight);
            obstacleUp.setTranslateX(i * 400 + width);
            obstacleUp.setTranslateY(pipeHeight + hole);
            obstacles.add(obstacleUp);

            gamePane.getChildren().addAll(obstacleDown, obstacleUp);
        }
    }


    public void move() {

        if(userController.speed.getY() < 5) {
            userController.speed = userController.speed.add(0,1);
        }

        bird.moveX((int)userController.speed.getX());
        bird.moveY((int)userController.speed.getY());
        scoreLabel.setText("Score: " + score);

        bird.translateXProperty().addListener((observable, oldValue, newValue) ->
        {
            int shift = newValue.intValue();
            if (shift > 200)gamePane.setLayoutX(-(shift - 200));
        });
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(windowController());
        scene.setOnMouseClicked(event -> userController.jump());
        primaryStage.setScene(scene);
        primaryStage.show();


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
            }
        };
        timer.start();

    }

    public static void main (String[] args) {
        launch(args);
    }

}


