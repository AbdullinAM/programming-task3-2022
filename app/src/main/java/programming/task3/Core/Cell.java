package programming.task3.Core;
import static programming.task3.Core.Checkers.*;



public class Cell {

    private  int quantity;

    private Checkers colour;

    public Cell() {
        quantity = 0;
        colour = NO_COLOR;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Checkers getColour() {
        return colour;
    }

    public void setColour(Checkers colour) {
        this.colour = colour;
    }

    public boolean isOccupied() {
        return colour != NO_COLOR;
    }
}
