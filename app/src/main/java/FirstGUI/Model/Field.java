package FirstGUI.Model;

public class Field {

    private final GroupOfChips[][] field = {{
                new GroupOfChips(15, ChipColor.WHITE),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null)
            }
            ,
            {
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null)
            }
            ,
            {
                new GroupOfChips(15, ChipColor.BLACK),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null)
            },
            {
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null),
                new GroupOfChips(0, null)
            }
    };

    public GroupOfChips[][] getCurrent() {
        return field;
    }

    public GroupOfChips get(int idx){
        if(idx>23) idx = idx%24;
        return field[idx/6][idx%6];
    }

    public void moveChip(int from, int to, boolean whiteExitOpened, boolean blackExitOpened){
        if(from>24) from = from%24;
        if(to>24) to = to%24;
        GroupOfChips start = field[from/6][from%6];
        GroupOfChips end = field[to/6][to%6];
        if (end.getQuantity()==0){
            end.setColor(start.getColor());
        }
        end.increaseQuantity();
        start.decreaseQuantity();
        if (start.getQuantity()==0){
            start.setColor(null);
        }
        //  огда игрок завел все фишки в последнюю четверть и выводит с пол€, просто удал€ем фишки
        if (whiteExitOpened && field[from/6][from%6].getColor() == ChipColor.WHITE) field[from/6][from%6].decreaseQuantity();
        if (blackExitOpened && field[from/6][from%6].getColor() == ChipColor.BLACK) field[from/6][from%6].decreaseQuantity();

    }
}
