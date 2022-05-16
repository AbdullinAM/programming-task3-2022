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
        end.increaseQuantity(start.getColor());
        start.decreaseQuantity();

        // Когда игрок завел все фишки в последнюю четверть и выводит с поля, просто удаляем фишки
        if (whiteExitOpened && start.getColor() == ChipColor.WHITE) end.decreaseQuantity();
        if (blackExitOpened && start.getColor() == ChipColor.BLACK) end.decreaseQuantity();

    }
}
