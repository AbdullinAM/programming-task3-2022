package FirstGUI.Model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Model {

    public Model() {

    }

    /*Кости*/
    private final Dice dice = new Dice();

    /*Текущий ход и розыгрыш права первого хода*/
    private ChipColor currentTurn = ChipColor.WHITE;

    public ChipColor getCurrentTurn() {
        return currentTurn;
    }

    public void randomFirstTurn() {
        dice.rollDice();
        if (dice.getValue()>=4) {
            currentTurn = ChipColor.BLACK;
        }
    }

    private int turnsCounter = 0;

    /*Список оставшихся ходов*/
    private List<Integer> turnsLeft = new ArrayList<>();

    public List<Integer> getTurnsLeft() {
        return turnsLeft;
    }

    public void setTurnsLeft(List<Integer> turnsList) { this.turnsLeft = turnsList;}

    public void rollNewTurns() {
        if (turnsLeft.isEmpty()) {
            dice.rollDice();
            turnsLeft.add(dice.getValue());
//            turnsLeft.add(6);
            dice.rollDice();
            turnsLeft.add(dice.getValue());
//            turnsLeft.add(6);
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
        currentTurn = (currentTurn == ChipColor.WHITE)? ChipColor.BLACK : ChipColor.WHITE;
    }


    /*Поле*/
    private Field field = new Field();

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    /*Ходы и Listener для вызова отрисовки на контроллере*/

    @NotNull
    public List<Integer> getPossibleTurns (int position) {
        ChipColor targetColor;
        List<Integer> result = new ArrayList<>();
        ChipColor color = field.get(position).getColor();
        if (turnsLeft.isEmpty()) return result;
        if (turnFromBaseHappened && (position == 0 || position == 12)) return result;
        /*Добавляем ходы исходя из стартовой позиции и имеющихся ходов (TurnsLeft)*/
        /*Если ходов 2 или больше (дубль)*/
        if (turnsLeft.size() >= 2) {
            int turn1 = turnsLeft.get(0);
            int turn2 = turnsLeft.get(1);
            targetColor = field.get(position+turn1).getColor();
            if (targetColor == color || targetColor == null){
                result.add((position+turn1)%24);
            }
            targetColor = field.get(position+turn2).getColor();
            if (targetColor == color || targetColor == null){
                result.add((position+turn2)%24);
            }
            if (!result.isEmpty()) {
                targetColor = field.get(position + turn1 + turn2).getColor();
                if (targetColor == color || targetColor == null) {
                    result.add((position + turn1 + turn2)%24);
                }
            }
        } else /*Если ход остался один*/ {
            GroupOfChips target = field.get(position + turnsLeft.get(0));
            targetColor = target.getColor();
            if (targetColor == color || targetColor == null)
                result.add((position + turnsLeft.get(0))%24);
        }
        /*Удаляем ходы которые противоречат правилу гласящему что выходить с поля фишки могут только тогда
        когда все фишки одного цвета добрались до последней четверти своего пути*/
        if (color == ChipColor.WHITE && position > 11 && !whiteExitOpened) {
            List<Integer> targetsToRemove = new ArrayList<>();
            result.forEach(target -> {
                if (target >=0 && target <= 11) targetsToRemove.add(target);
            });
            result.removeAll(targetsToRemove);
        }
        if (color == ChipColor.BLACK && position > 0 && position < 12 && !blackExitOpened) {
            List<Integer> targetsToRemove = new ArrayList<>();
            result.forEach(target -> {
                if (target >= 12) targetsToRemove.add(target);
            });
            result.removeAll(targetsToRemove);
        }
        return result;
    }

    public void makeTurn(int startPosition, int targetPosition){
        //меняем поле
        field.moveChip(startPosition, targetPosition, whiteExitOpened, blackExitOpened);
        //меняем turnsLeft и currentTurn
        if (turnsLeft.size() >= 2){
            int maxTurn = turnsLeft.get(0)+turnsLeft.get(1);
            int delta = targetPosition - startPosition < 0? (24-startPosition+targetPosition) : (targetPosition - startPosition);
            if (delta == maxTurn){
                turnsLeft.remove(0);
                turnsLeft.remove(0);
                if(turnsLeft.isEmpty()) currentTurn = currentTurn==ChipColor.WHITE?ChipColor.BLACK:ChipColor.WHITE;
            } else if (delta == turnsLeft.get(0)) {
                turnsLeft.remove(0);
            } else if (delta == turnsLeft.get(1)){
                turnsLeft.remove(1);
            } else return;
        } else if (turnsLeft.size() == 1) {
            turnsLeft.remove(0);
            currentTurn = currentTurn==ChipColor.WHITE?ChipColor.BLACK:ChipColor.WHITE;
        } else return;
        // Можно сделать только один ход с базы, поэтому поднимаем соответствующий флаг если ход с базы.
        // Первый бросок с головы, в начале игры (партии) предоставляет игрокам исключение из вышеуказанного правила.
        // Если одна шашка, которую только и можно снять с головы, не проходит, то можно снять вторую.
        if (startPosition == 0 || startPosition == 12) turnFromBaseHappened = true;
        if (turnsCounter < 2 && getPossibleTurns(targetPosition).isEmpty()) {
            if (field.get(0).getColor() == currentTurn && field.get(0).getQuantity() > 13)
                turnFromBaseHappened = false;
            if (field.get(12).getColor() == currentTurn && field.get(12).getQuantity() > 13)
                turnFromBaseHappened = false;
        }
        // Открываем выхода с поля игроку у которого все фишки в последней четверти
        openExitsIfPossible();
        listener.turnMade();
    }

    public ModelListener listener;

    /*Окончание игры*/

    private boolean whiteExitOpened;

    private boolean blackExitOpened;

    public void openExitsIfPossible(){
        boolean noWhitesOutOfHome = true;
        boolean noBlacksOutOfHome = true;
        for (int i = 0; i < 18; i++) {
            if (field.get(i).getColor() == ChipColor.WHITE) noWhitesOutOfHome = false;
        }
        for (int i = 12; i < 24; i++) {
            if (field.get(i).getColor() == ChipColor.BLACK) noBlacksOutOfHome = false;
        }
        for (int i = 0; i < 6; i++) {
            if (field.get(i).getColor() == ChipColor.BLACK) noBlacksOutOfHome = false;
        }
        whiteExitOpened = noWhitesOutOfHome;
        blackExitOpened = noBlacksOutOfHome;
    }


//  Вспомогательные
    public int howManyChipsInColumn(int column){
        return field.get(column).getQuantity();
    }
}
