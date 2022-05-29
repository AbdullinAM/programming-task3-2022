package FirstGUI.Model;

import java.util.ArrayList;
import java.util.List;

import static FirstGUI.Model.ChipColor.*;

public class Model {

    /*Текущий ход и розыгрыш права первого хода*/
    private ChipColor currentTurn = WHITE;

    public ChipColor getCurrentTurn() {
        return currentTurn;
    }

    private int turnsCounter = 0;

    /*Список оставшихся ходов*/
    private List<Integer> turnsLeft = new ArrayList<>();

    public ArrayList<Integer> getTurnsLeft() {
        return new ArrayList<>(turnsLeft);
    }

    public boolean isNoTurnsLeft() {
        return turnsLeft.isEmpty();
    }

    /*Use this method only for tests*/
    public void setTurnsLeft(List<Integer> turnsList) { this.turnsLeft = turnsList;}

    public void rollNewTurns() {
        if (turnsLeft.isEmpty()) {
            turnsLeft.add(Dice.rollDice());
            turnsLeft.add(Dice.rollDice());
            if (turnsLeft.get(0).equals(turnsLeft.get(1))) turnsLeft.addAll(turnsLeft);
            turnFromBaseHappened = false;
            turnsCounter += 1;
        }
    }

    /*С базы можно снять только одну фишку за полный ход*/
    public boolean turnFromBaseHappened = false;

    /*Передача хода оппоненту если не осталось вариантов ходов*/
    public void passTheTurn(){
        turnsLeft = new ArrayList<>();
        currentTurn = currentTurn.opposite();
    }


    /*Поле*/
    private final Field field = new Field();

    public Field getField() {
        return field;
    }

    /*Ходы и Listener для вызова отрисовки на контроллере*/

    /*Возвращает возможные ходы для какой-либо колонки фишек
    * Возвращает список чисел в котором могут быть числа от 0 до 24 (0-23 это позиции, а 24 означает выход с поля) */
    public List<Integer> getPossibleTurns (int position) {
        List<Integer> result = new ArrayList<>();
        ChipColor color = field.get(position).getColor();
        if (turnsLeft.isEmpty() || color == NO_COLOR) return result;
        if (turnFromBaseHappened && (position == 0 || position == 12)) return result;
        /*Добавляем ходы исходя из стартовой позиции и имеющихся ходов (TurnsLeft)*/
        /*Если ходов 2 и более */
        if (turnsLeft.size() >= 2) {
            int turn1 = turnsLeft.get(0);
            int turn2 = turnsLeft.get(1);
            int target = position+turn1;
            checkIfTurnPossible(target, color, result);
            target = position+turn2;
            checkIfTurnPossible(target, color, result);
            target = position + turn1 + turn2;
            checkIfTurnPossible(target, color, result);
        } else /*Если ход остался один*/ {
            int turn = turnsLeft.get(0);
            int target = position + turn;
            checkIfTurnPossible(target, color, result);
        }
        /*Теперь удаляем ходы в соответствии с правилами*/
        List<Integer> targetsToRemove = new ArrayList<>();
        /*Правило захода в дом. Выходить с поля фишки могут только тогда
        когда все фишки одного цвета добрались до последней четверти своего пути*/
        boolean exitOpened = color == WHITE? whiteExitOpened: blackExitOpened;
        if (!exitOpened) {
            if (color == WHITE)
                result.forEach(target -> {
                    if (position > 11 && target <= 11)
                        targetsToRemove.add(target);
                }
                );
            if (color == BLACK)
                result.forEach(target -> {
                    if (position <= 11 && target > 11)
                        targetsToRemove.add(target);
                }
                );
        }
        /*Удаляем ходы которые противоречат правилу блокирования
        (нельзя ставить 6 подряд если впереди нет фишки противника)*/
        result.forEach(target ->{
            if (target != 24)
                if (field.willItBlock(position, target)) targetsToRemove.add(target);
        });
        result.removeAll(targetsToRemove);
        /*Если оба turnsLeft удалены из-за каких-то правил, то не забываем удалить и суммарный ход*/
        if (turnsLeft.size() > 1)
            if(!result.contains((position+turnsLeft.get(0))%24)
                    && !result.contains((position + turnsLeft.get(1))%24))
                result.remove((Object)((position + turnsLeft.get(0) + turnsLeft.get(1))%24));
        return result;
    }

    private void checkIfTurnPossible(int target, ChipColor baseColor, List<Integer> result) {
        ChipColor targetColor = field.get(target).getColor();
        boolean exitOpened = baseColor == WHITE? whiteExitOpened : blackExitOpened;
        /*Если выход закрыт, то просто добавляем ход (проверки на правила будут позже)*/
        if ((targetColor == baseColor || targetColor == NO_COLOR) && !exitOpened)
            result.add((target) % 24);
        /*Если выход открыт и в результате хода фишка может выйти с поля, то добавляем 24 */
        if (baseColor == WHITE && whiteExitOpened)
            if(target > 23)
                result.add(24);
            else result.add((target) % 24);
        if (baseColor == BLACK && blackExitOpened)
            if(target > 11)
                result.add(24);
            else result.add((target) % 24);
    }


    public void makeTurn(int startPosition, int targetPosition){
        //меняем поле
        field.moveChip(startPosition, targetPosition);
        //меняем turnsLeft и currentTurn
        if (turnsLeft.size() >= 2){
            int maxTurn = turnsLeft.get(0)+turnsLeft.get(1);
            int delta = targetPosition - startPosition < 0? (24-startPosition+targetPosition) : (targetPosition - startPosition);
            if (blackExitOpened && targetPosition == 24 && currentTurn == BLACK) delta -= 12;
            if (delta == maxTurn){
                turnsLeft.remove(0);
                turnsLeft.remove(0);
                if(turnsLeft.isEmpty()) currentTurn = currentTurn.opposite();
            } else if (delta == turnsLeft.get(0)) {
                turnsLeft.remove(0);
            } else if (delta == turnsLeft.get(1)){
                turnsLeft.remove(1);
            } else {
                boolean exitOpened = currentTurn == WHITE? whiteExitOpened: blackExitOpened;
                if (exitOpened) {
                    Integer smallerTurn = turnsLeft.get(0) < turnsLeft.get(1) ? turnsLeft.get(0) : turnsLeft.get(1);
                    Integer biggerTurn = turnsLeft.get(0) > turnsLeft.get(1) ? turnsLeft.get(0) : turnsLeft.get(1);
                    if (delta <= smallerTurn) turnsLeft.remove(smallerTurn);
                    else if (delta <= biggerTurn) turnsLeft.remove(biggerTurn);
                    else {
                        turnsLeft.remove(smallerTurn);
                        turnsLeft.remove(biggerTurn);
                        currentTurn = currentTurn.opposite();
                    }
                }
            }
        } else if (turnsLeft.size() == 1) {
            turnsLeft.remove(0);
            currentTurn = currentTurn.opposite();
        }
        // Можно сделать только один ход с базы, поэтому поднимаем соответствующий флаг если ход с базы.
        // Первый бросок с головы, в начале игры (партии) предоставляет игрокам исключение из вышеуказанного правила.
        // Если одна шашка, которую только и можно снять с головы, не проходит, то можно снять вторую.
        if ((startPosition == 0 && currentTurn == WHITE) || (startPosition == 12 && currentTurn == BLACK)) turnFromBaseHappened = true;
        if (turnsCounter <= 2 && getPossibleTurns(targetPosition).isEmpty()) {
            if (field.get(0).getColor() == currentTurn && field.get(0).getQuantity() > 13)
                turnFromBaseHappened = false;
            if (field.get(12).getColor() == currentTurn && field.get(12).getQuantity() > 13)
                turnFromBaseHappened = false;
        }
        // Открываем выход с поля игроку у которого все фишки в последней четверти
        openExitsIfPossible();
        listener.turnMade();
    }

    private ModelListener listener;

    public Model setModelListener(ModelListener l){
        listener = l;
        return this;
    }

    /*Окончание игры*/

    private boolean whiteExitOpened;

    private boolean blackExitOpened;

    public void openExitsIfPossible(){
        boolean noWhitesOutOfHome = true;
        boolean noBlacksOutOfHome = true;
        for (int i = 0; i < 18; i++) {
            if (field.get(i).getColor() == WHITE) noWhitesOutOfHome = false;
        }
        for (int i = 12; i < 30; i++) {
            if (field.get(i).getColor() == BLACK) noBlacksOutOfHome = false;
        }
        whiteExitOpened = noWhitesOutOfHome;
        blackExitOpened = noBlacksOutOfHome;
    }


//  Вспомогательные
    public int howManyChipsInColumn(int column){
        return field.get(column).getQuantity();
    }

    public void restart() {
        currentTurn = WHITE;
        turnsLeft = new ArrayList<>();
        turnsCounter = 0;
        field.clear();
        listener.turnMade();
    }
}
