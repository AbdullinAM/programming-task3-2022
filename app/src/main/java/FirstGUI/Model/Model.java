package FirstGUI.Model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class Model {

    /*����� ������� ��� scoreboard*/

    public Model() {
        this.randomFirstTurn();
    }


    /*�����*/
    private final Dice dice = new Dice();

    /*������� ��� � �������� ����� ������� ����*/
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

    /*������ ���������� �����*/
    private List<Integer> turnsLeft = new ArrayList<>();

    public List<Integer> getTurnsLeft() {
        return turnsLeft;
    }

    public void rollNewTurns() {
        if (turnsLeft.isEmpty()) {
            dice.rollDice();
            turnsLeft.add(dice.getValue());
            dice.rollDice();
            turnsLeft.add(dice.getValue());
            if (turnsLeft.get(0).equals(turnsLeft.get(1))) turnsLeft.addAll(turnsLeft);
            turnFromBaseHappened = false;
        }
    }

    /*� ���� ����� ����� ������ ���� ����� �� ���*/
    public boolean turnFromBaseHappened = false;

    /*�������� ���� ��������� ���� �� �������� ��������� �����*/
    public void passTheTurn(){
        turnsLeft = new ArrayList<>();
        currentTurn = (currentTurn == ChipColor.WHITE)? ChipColor.BLACK : ChipColor.WHITE;
    }


    /*����*/
    private Field field = new Field();

    public Field getField() {
        return field;
    }

    /*���� � Listener ��� ������ ��������� �� �����������*/
    public ModelListener listener;

    private boolean whiteExitOpened;

    private boolean blackExitOpened;

    @NotNull
    public List<Integer> getPossibleTurns (int position) {
        List<Integer> result = new ArrayList<>();
        ChipColor color = field.get(position).getColor();
        ChipColor targetColor;
        if (turnsLeft.isEmpty()) return result;
        if (turnFromBaseHappened && (position == 0 || position == 12)) return result;
        /*��������� ���� ������ �� ��������� ������� � ��������� ����� (TurnsLeft)*/
        /*���� ����� 2 ��� ������ (�����)*/
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
        } else /*���� ��� ������� ����*/ {
            GroupOfChips target = field.get(position + turnsLeft.get(0));
            targetColor = target.getColor();
            if (targetColor == color || targetColor == null)
                result.add((position + turnsLeft.get(0))%24);
        }
        /*������� ���� ������� ������������ ������� ��������� ��� �������� � ���� ����� ����� ������ �����
        ����� ��� ����� ������ ����� ��������� �� ��������� �������� ������ ����*/
        if (color == ChipColor.WHITE && position > 17 && !whiteExitOpened) {
            List<Integer> targetsToRemove = new ArrayList<>();
            result.forEach(target -> {
                if (target >0 && target < 11) targetsToRemove.add(target);
            });
            result.removeAll(targetsToRemove);
        }
        if (color == ChipColor.BLACK && position > 5 && position < 12 && !blackExitOpened) {
            List<Integer> targetsToRemove = new ArrayList<>();
            result.forEach(target -> {
                if (target > 13) targetsToRemove.add(target);
            });
            result.removeAll(targetsToRemove);
        }
        return result;
    }

    public void makeTurn(int startPosition, int targetPosition){
        //������ ����
        field.moveChip(startPosition, targetPosition, whiteExitOpened, blackExitOpened);
        //������ turnsLeft � currentTurn
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
        // ����� ������� ������ ���� ��� � ����, ������� ��������� ��������������� ���� ���� ��� � ����
        if (startPosition == 0 || startPosition == 12) turnFromBaseHappened = true;
        // ��������� ������ � ���� ������ � �������� ��� ����� � ��������� ��������
        openExitsIfPossible();
        listener.turnMade();
    }

    private void openExitsIfPossible(){
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

//  ���������������
    public int howManyChipsInColumn(int column){
        return field.get(column).getQuantity();
    }
}
