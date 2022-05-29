package FirstGUI.Model;

public enum ChipColor {
    /**/
    BLACK,
    WHITE,
    NO_COLOR;

    public ChipColor opposite() {
        return this == WHITE? BLACK : WHITE;
    }
}
