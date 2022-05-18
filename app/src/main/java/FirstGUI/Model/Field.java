package FirstGUI.Model;

import java.awt.*;

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
        start.decreaseQuantity();

        // Когда игрок завел все фишки в последнюю четверть и выводит с поля, просто удаляем фишки
        if (whiteExitOpened && start.getColor() == ChipColor.WHITE) end.decreaseQuantity();
        if (blackExitOpened && start.getColor() == ChipColor.BLACK) end.decreaseQuantity();

    }

    public boolean willItBlock(ChipColor color, int targetPos){
        int similarChipsCounter = 0;
        for (int i = 1; i < 6; i++) {
            if (get(targetPos-i).getColor()==color) similarChipsCounter += 1; else break;
        }
        for (int i = 1; i < 6; i++) {
            if (get(targetPos+i).getColor()==color) similarChipsCounter += 1; else break;
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

}
