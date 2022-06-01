package FirstGUI.Model;

import java.util.ArrayList;
import java.util.List;

import static FirstGUI.Model.ChipColor.*;

public class Field {

    /*Следующая структура данных появилась с идеей что удобно будет иметь возможность легко обращаться к четвертям поля,
    * но потом это так и не понадобилось. Переделаю в arrayList как руки дойдут*/
    private final List<GroupOfChips> field = new ArrayList<>();

    public static final int fieldSize = 24;

    public static final int whiteBase = 0;

    public static final int blackBase = 12;

    public static final int exit = 24;

    public Field() {
        for (int i = 0; i < fieldSize; i++) {
            field.add(new GroupOfChips());
        }
        get(whiteBase).setColor(WHITE).setQuantity(15);
        get(blackBase).setColor(BLACK).setQuantity(15);
    }

    public GroupOfChips get(int idx){
        if(idx < 0) {
            idx = fieldSize + idx % fieldSize;
        }
        if(idx >= fieldSize) {
            idx %= fieldSize;
        }
        return field.get(idx);
    }

    public void set(int idx, GroupOfChips chips){
        if (idx >= fieldSize) {
            idx %= fieldSize;
        }
        field.set(idx, chips);
    }

    public void moveChip(int from, int to){
        if (get(from).getQuantity() == 0 || get(from).getColor() == NO_COLOR) {
            throw new IllegalArgumentException("Trying to make turn from position that's empty");
        }
        if (to == exit) {
            get(from).decreaseQuantity();
            return;
        }
        if(from >= fieldSize) {
            from %= fieldSize;
        }
        if(to > fieldSize) {
            to %= fieldSize;
        }
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
                for (int i = whiteBase; i < whiteBase + fieldSize; i++) {
                    if(get(i).getColor() == color) {
                        similarChipsCounter++;
                    } else {
                        similarChipsCounter = 0;
                    }
                    if (similarChipsCounter > 5) {
                        willItBlock = true;
                        for (int j = i; j < fieldSize; j++) {
                            if (get(j).getColor() == WHITE) {
                                willItBlock = false;
                            }
                        }
                    }
                }
            }
            case WHITE -> {
                /*Идём по пути черных и если находим 6 белых подряд то проверяем чтобы спереди них уже была черная, иначе block*/
                for (int i = blackBase; i < blackBase + fieldSize; i++) {
                    if(get(i).getColor() == color) {
                        similarChipsCounter++;
                    } else {
                        similarChipsCounter = 0;
                    }
                    if (similarChipsCounter > 5) {
                        willItBlock = true;
                        for (int j = i; j < blackBase + fieldSize; j++) {
                            if (get(j).getColor() == BLACK) {
                                willItBlock = false;
                            }
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
        for (int i = 0; i < fieldSize; i++) {
            if (get(i).getColor() == WHITE) {
                whiteWON = false;
            }
            if (get(i).getColor() == BLACK) {
                blackWON = false;
            }
        }
        if (whiteWON)
            return WHITE;
        if (blackWON)
            return BLACK;
        return null;
    }

    public void clear() {
        for (int i = 1; i < fieldSize; i++) {
            get(i).setColor(NO_COLOR).setQuantity(0);
        }
        get(whiteBase).setQuantity(15).setColor(WHITE);
        get(blackBase).setQuantity(15).setColor(BLACK);
    }

}
