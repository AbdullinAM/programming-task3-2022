package programming.task3.Core;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.List;

public enum Checkers {
    WHITE(new ImagePattern(new Image("/White.png"))),
    BLACK(new ImagePattern(new Image("/Black.png"))),
    NO_COLOR(new ImagePattern(new Image("/wood.png")));
    ImagePattern imagePattern;

    Checkers(ImagePattern i) {
        this.imagePattern = i;
    }

    public ImagePattern getImagePattern() {
        return imagePattern;
    }

    public Checkers opposite() {
        if (this == WHITE) return BLACK;
        else return WHITE;
    }




}




