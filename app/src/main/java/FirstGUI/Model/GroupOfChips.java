package FirstGUI.Model;

import javafx.scene.paint.Color;

public class GroupOfChips {
    private Integer quantity;

    private ChipColor color;


    public GroupOfChips(Integer quantity, ChipColor color) {
        this.quantity = quantity;
        this.color = color;
    }

    public void setQuantity(Integer quantity) {
        if(quantity >= 0 && quantity < 16){
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException();}
    }

    public Integer getQuantity() { return quantity; }

    public void setColor(ChipColor color) { this.color = color; }

    public ChipColor getColor() { return color; }

    public Color getNormalizedColor() { return color.normalizedColor; }

    public void decreaseQuantity(){ quantity -= 1; }

    public void increaseQuantity() { quantity +=1; }
}
