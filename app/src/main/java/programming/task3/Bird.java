package programming.task3;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class Bird extends UserController {

    private Rectangle rect;
    public Bird() {
        rect = new Rectangle(52, 40, new ImagePattern(new Image("img.png")));
        getChildren().addAll(rect);
    }

    public void moveX(int value) {
        super.moveX(value);
    }

    public void moveY(int value) {
        super.moveY(value);
    }

    public void jump() {
        super.jump();
    }
}

