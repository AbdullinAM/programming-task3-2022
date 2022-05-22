package FirstGUI.Model;

public class GroupOfChips {
    private Integer quantity;

    private ChipColor color;

    public GroupOfChips(){
        this.quantity = 0;
    }

    public GroupOfChips(Integer quantity, ChipColor color) {
        this.quantity = quantity;
        this.color = color;
    }

    public GroupOfChips setQuantity(Integer quantity) {
        if(quantity >= 0 && quantity < 16){
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException();}
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
            setColor(null);
        }}

    public void increaseQuantity(ChipColor color) {
        if (quantity==0){
            this.color = color;
        }
        quantity +=1;
        }
}
