package programming.task3.Core;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public enum Checkers {
    WHITE,

    BLACK,

    NO_COLOR;

//    ImagePattern imagePattern;
//
//    Checkers(ImagePattern i) {
//        this.imagePattern = i;
//    }
//
//    public ImagePattern getImagePattern() {
//        return imagePattern;
//    }

    public Checkers opposite() {
        if (this == WHITE) return BLACK;
        else return WHITE;
    }
}




