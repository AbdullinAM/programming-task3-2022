package FirstGUI.Model;

import javafx.scene.paint.Color;

public enum ChipColor {
    BLACK(new Color(0.0,0.0,0.0,1.0)),
    WHITE(new Color(1.0,1.0,1.0,1.0));

    ChipColor(Color color){
        this.normalizedColor = color;
    }

    public final Color normalizedColor;
}
