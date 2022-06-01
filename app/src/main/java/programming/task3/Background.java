package programming.task3;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class Background extends Pane {
    public Background() {
        Rectangle sky = new Rectangle(600, 600, new ImagePattern(new Image("backgroundd.png")));
        getChildren().addAll(sky);
    }
}
