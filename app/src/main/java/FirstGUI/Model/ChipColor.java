package FirstGUI.Model;

public enum ChipColor {
    BLACK,
    WHITE;

    public ChipColor opposite() {
        return this == WHITE? BLACK : WHITE;
    }
}
