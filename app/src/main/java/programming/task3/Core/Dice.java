package programming.task3.Core;



public class Dice {
    private int dice;

    public int throwDice() {
        dice = (int) (Math.random() * 6) + 1;
        return dice;
    }
}
