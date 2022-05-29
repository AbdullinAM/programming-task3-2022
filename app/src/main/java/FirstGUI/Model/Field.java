package FirstGUI.Model;

import java.util.ArrayList;
import java.util.List;

import static FirstGUI.Model.ChipColor.*;

public class Field {

    /*Следующая структура данных появилась с идеей что удобно будет иметь возможность легко обращаться к четвертям поля,
    * но потом это так и не понадобилось. Переделаю в arrayList как руки дойдут*/
    private final List<GroupOfChips> field = new ArrayList<>();

    public Field() {
        field.addAll(List.of(
                new GroupOfChips(15, ChipColor.WHITE),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(15, ChipColor.BLACK),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips(),
                new GroupOfChips()
                )
        );
    }

    public GroupOfChips get(int idx){
        if(idx<0) idx = 24 + idx%24;
        if(idx>23) idx = idx%24;
        return field.get(idx);
    }

    public void set(int idx, GroupOfChips chips){
        if (idx > 23) idx = idx%24;
        field.set(idx, chips);
    }

    public void moveChip(int from, int to){
        if (get(from).getQuantity() == 0 || get(from).getColor() == NO_COLOR)
            throw new IllegalArgumentException("Trying to make turn from position that's empty");
        if (to == 24) {
            get(from).decreaseQuantity();
            return;
        }
        if(from>=24) from = from%24;
        if(to>24) to = to%24;
        get(to).increaseQuantity(get(from).getColor());
        get(from).decreaseQuantity();
    }

    /*Проверяет нарушит ли какой-то ход запрет преграждения пути шестью шашками*/
    public boolean willItBlock(int startPos, int targetPos){
        ChipColor color = get(startPos).getColor();
        get(startPos).decreaseQuantity();
        get(targetPos).increaseQuantity(color);
        int similarChipsCounter = 0;
        boolean willItBlock = false;
        switch (color) {
            case BLACK -> {
                /*Идём по пути белых и если находим 6 черных подряд то проверяем чтобы спереди них уже была белая, иначе block*/
                for (int i = 0; i < 24; i++) {
                    if(get(i).getColor() == color) similarChipsCounter++;
                        else similarChipsCounter = 0;
                    if (similarChipsCounter > 5) {
                        willItBlock = true;
                        for (int j = i; j < 24; j++) {
                            if (get(j).getColor() == WHITE) willItBlock = false;
                        }
                    }
                }
            }
            case WHITE -> {
                /*Идём по пути черных и если находим 6 белых подряд то проверяем чтобы спереди них уже была черная, иначе block*/
                for (int i = 11; i < 36; i++) {
                    if(get(i).getColor() == color) similarChipsCounter++;
                        else similarChipsCounter = 0;
                    if (similarChipsCounter > 5) {
                        willItBlock = true;
                        for (int j = i; j < 35; j++) {
                            if (get(j).getColor() == BLACK) willItBlock = false;
                        }
                    }
                }
            }

        }
        get(startPos).increaseQuantity(color);
        get(targetPos).decreaseQuantity();
        return willItBlock;
    }

    public ChipColor winnerIs () {
        boolean whiteWON = true;
        boolean blackWON = true;
        for (int i = 0; i <= 23; i++) {
            if (get(i).getColor() == WHITE) whiteWON = false;
            if (get(i).getColor() == BLACK) blackWON = false;
        }
        if (whiteWON) return WHITE;
        if (blackWON) return BLACK;
        return null;
    }

    public void clear(){
        for (int i = 1; i < 24; i++) {
            get(i).setColor(NO_COLOR).setQuantity(0);
        }
        get(0).setQuantity(15).setColor(WHITE);
        get(12).setQuantity(15).setColor(BLACK);
    }

}
