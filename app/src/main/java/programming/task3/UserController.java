package programming.task3;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;


public class UserController extends Pane {
    public Point2D speed = new Point2D(0,0);

    public void moveY(int value){
        boolean moveDown = value > 0;
        for(int i = 0; i < Math.abs(value); i++) {
            for(Obstacle obstacle : Main.obstacles) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())){
                    if (moveDown) {
                        setTranslateX(0);
                        Main.score = 0;
                        return;
                    }
                    else {
                        setTranslateY(getTranslateY() + 1);
                        return;
                    }
                }
            }

            if (getTranslateY() < 0) {
                setTranslateY(0);
            }
            if (getTranslateY() > 580) {
                setTranslateY(580);
            }
            setTranslateY(getTranslateY() + (moveDown ? 1 : -1));
        }
    }

    public void moveX(int value) {
        for (int i = 0; i < value; i++) {
            setTranslateX(getTranslateX() + 1);
            for (Obstacle obstacle : Main.obstacles) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (getTranslateX() + 20 == obstacle.getTranslateX()) {
                        setTranslateX(0);
                        Main.score = 0;
                        return;
                    }
                }
                if (getTranslateX() == obstacle.getTranslateX()) {
                    Main.score++;
                    return;
                }
            }

        }
    }


    public void jump(){
        speed = new Point2D(3, -9);
    }

}
