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

    public GroupOfChips[][] getQuarters() {
        return field;
    }

    public GroupOfChips get(int idx){
        if(idx<0) idx = 24 + idx%24;
        if(idx>23) idx = idx%24;
        return field[idx/6][idx%6];
    }

    public void set(int idx, GroupOfChips chips){
        field[idx/6][idx%6] = chips;
    }

    public void moveChip(int from, int to, boolean whiteExitOpened, boolean blackExitOpened){
        if(from>24) from = from%24;
        if(to>24) to = to%24;
        GroupOfChips start = field[from/6][from%6];
        GroupOfChips end = field[to/6][to%6];
        end.increaseQuantity(start.getColor());
        // Когда игрок завел все фишки в последнюю четверть и выводит с поля, просто удаляем фишки
        if (whiteExitOpened && start.getColor() == ChipColor.WHITE && to >= 0 & to < 17) end.decreaseQuantity();
        if (blackExitOpened && start.getColor() == ChipColor.BLACK && to >= 12) end.decreaseQuantity();
        start.decreaseQuantity();

    }

    /*Проверяет нарушит ли какой-то ход запрет преграждения пути шестью шашками*/
    public boolean willItBlock(ChipColor color, int targetPos){
        int similarChipsCounter = 0;
        for (int i = 1; i < 6; i++) {
            if (get(targetPos-i).getColor()==color) {
                if(color == ChipColor.WHITE && targetPos-i == 12) break;
                if(color == ChipColor.BLACK && (targetPos-i)%24 == 0) break;
                similarChipsCounter += 1;
            } else break;
        }
        for (int i = 1; i < 6; i++) {
            if (get(targetPos+i).getColor()==color){
                if(color == ChipColor.WHITE && targetPos+i == 12) break;
                if(color == ChipColor.BLACK && (targetPos+i)%24 == 0) break;
                similarChipsCounter += 1;
            } else break;
        }
        if (similarChipsCounter >= 5) {
            if (color == ChipColor.BLACK){
                for (int i = targetPos; i < 24; i++) {
                    if (get(i).getColor() == ChipColor.WHITE) return false;
                }
                return true;
            }
            if (targetPos <= 11)
                for (int i = targetPos; i <= 11; i++) {
                    if (get(i).getColor()== ChipColor.BLACK) return false;
                }
            else
                for (int i = targetPos; i <= 35; i++) {
                    if (get(i).getColor()== ChipColor.BLACK) return false;
                }
            return true;
        }
        return false;
    }

    public ChipColor winnerIs () {
        boolean whiteWON = true;
        boolean blackWON = true;
        for (int i = 0; i <= 23; i++) {
            if (get(i).getColor() == ChipColor.WHITE) whiteWON = false;
            if (get(i).getColor() == ChipColor.BLACK) blackWON = false;
        }
        if (whiteWON) return ChipColor.WHITE;
        if (blackWON) return ChipColor.BLACK;
        return null;
    }

    public void clear(){
        for (int i = 1; i < 24; i++) {
            get(i).setColor(null).setQuantity(0);
        }
        get(0).setQuantity(15).setColor(ChipColor.WHITE);
        get(12).setQuantity(15).setColor(ChipColor.BLACK);
    }

}
