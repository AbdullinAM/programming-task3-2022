package FirstGUI.Model;
import java.util.Random;

public class Dice {
    private int value = 0;
    private final Random rand = new Random();

    public void rollDice() {
        value = rand.nextInt(6);
    }

    public int getValue() {
        return value + 1;
    }
}
