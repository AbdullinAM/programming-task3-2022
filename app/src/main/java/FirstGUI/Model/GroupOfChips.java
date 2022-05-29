package FirstGUI.Model;
import static FirstGUI.Model.ChipColor.*;

public class GroupOfChips {
    private Integer quantity;

    private ChipColor color;

    public GroupOfChips(){
        this(0, NO_COLOR);
    }

    public GroupOfChips(Integer quantity, ChipColor color) {
        this.quantity = quantity;
        this.color = color;
    }

    public GroupOfChips setQuantity(Integer quantity) {
        if(quantity >= 0 && quantity < 16){
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity of chips should be 0..15");}
        return this;
    }

    public Integer getQuantity() { return quantity; }

    public GroupOfChips setColor(ChipColor color) {
        this.color = color;
        return this;
    }

    public ChipColor getColor() { return color; }

    public void decreaseQuantity(){
        quantity -= 1;
        if (quantity==0){
            color = NO_COLOR;
        }}

    public void increaseQuantity(ChipColor color) {
        if(this.color == color.opposite())
            throw new IllegalArgumentException("Tried to make turn into opponent's position");
        if (quantity==0){
            this.color = color;
        }
        quantity +=1;
        }
}
