package FirstGUI.Model;

public class Player {
    private ChipColor color;

    private String name;

    public Player(String name, ChipColor color) {
        this.color = color;
        this.name = name;
    }

    public ChipColor getColor() {
        return color;
    }

    public void setColor(ChipColor color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
