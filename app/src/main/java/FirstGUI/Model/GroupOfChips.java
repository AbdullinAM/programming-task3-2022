package FirstGUI.Model;

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
