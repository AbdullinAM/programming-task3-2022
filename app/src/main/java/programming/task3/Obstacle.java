package programming.task3;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Obstacle extends Pane {
    private Rectangle rect;
    private int height;

    public Obstacle(int height){
        this.height = height;
        rect = new Rectangle(40, height, Color.YELLOWGREEN);

        getChildren().add(rect);
    }

}
