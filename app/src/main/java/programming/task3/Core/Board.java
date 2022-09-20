package programming.task3.Core;

import programming.task3.Controller.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Board {


    public Checkers turn = Checkers.WHITE;

    private BoardListener listener = null;

    private final Cell[] board = new Cell[24];

    public final List<Integer> turns = new ArrayList<>();

    private Integer turnsFromHead = 0;

    Dice dice = new Dice();


    public Board(){
        for (int i = 0; i < 24; i++) {board[i] = new Cell();}
        board[0].setColour(Checkers.WHITE);
        board[0].setQuantity(15);
        board[12].setColour(Checkers.BLACK);
        board[12].setQuantity(15);
    }

    public Cell[] getBoard(){return board;}

    public Checkers getTurn() {
        return turn;
    }

    public  void setBoardListener(BoardListener l){
        listener = l;
    }

    public void turnOpposite(){
        turn = turn.opposite();
        turnsFromHead = 0;
        throwDices();
    }

    public void throwDices(){
        turns.clear();
        if (turns.isEmpty()){
            turns.add(dice.throwDice());
            turns.add(dice.throwDice());
            listener.dicesRolled(turns.get(0), turns.get(1));
            if (Objects.equals(turns.get(0), turns.get(1))){
                turns.add(turns.get(0));
                turns.add(turns.get(0));
            }
        }
    }

    private void diceDelete(int x, int finalPos){
        if (finalPos - x > 0) {
            if (turns.contains(finalPos - x)) {
                turns.remove(turns.indexOf(finalPos - x));
            } else {
                turns.remove(0);
                turns.remove(0);
            }
        } else {
            if (turns.contains(24 - x + finalPos)) {
                turns.remove(turns.indexOf(24 - x + finalPos));
            } else {
                turns.remove(0);
                turns.remove(0);
            }
        }
    }

    public void move (int x, int finalPos){
        if (board[finalPos].getQuantity() == 0){
            board[finalPos].setColour(board[x].getColour());
        }
        board[finalPos].setQuantity(board[finalPos].getQuantity() + 1);
        board[x].setQuantity(board[x].getQuantity() - 1);

        diceDelete(x, finalPos);

        if (board[x].getQuantity() == 0){
            board[x].setColour(Checkers.NO_COLOR);
        }
        if (board[x].getColour() == Checkers.WHITE && x == 0 || board[x].getColour() == Checkers.BLACK && x == 12){
            turnsFromHead += 1;
        }
        if (turns.isEmpty()) {
            turnOpposite();
        }
    }

    private void cantGoPastHouse(int x, List<Integer> allValidMoves){
        List<Integer> allValidMovesCopy = List.copyOf(allValidMoves);
        for (Integer i: allValidMovesCopy){
            if (board[x].getColour() == Checkers.WHITE && (x <= 23 && x > 11 && i >= 0 && i <= 11)){
                allValidMoves.remove(allValidMoves.indexOf(i));
            }
            if (board[x].getColour() == Checkers.BLACK && (x <= 11 && i >= 12)){
                allValidMoves.remove(allValidMoves.indexOf(i));
            }
        }
    }

    public List<Integer> allValidMoves(int x) {
        List<Integer> allValidMoves = new ArrayList<>();
        Checkers color = board[x].getColour();

        if ((board[x].getColour() == Checkers.WHITE && x == 0 ||
                board[x].getColour() == Checkers.BLACK && x == 12) && turnsFromHead > 0){
            return Collections.emptyList();
        }

        for (Integer t : turns) {
            int targetCell = (t + x) % 24;
            if (!board[targetCell].isOccupied() || board[targetCell].getColour() == color) {
                allValidMoves.add(targetCell);
                if (turns.size() >= 2) {
                    int targetCellTwoDices = (turns.get(0) + turns.get(1) + x) % 24;
                    if (!board[targetCellTwoDices].isOccupied() || board[targetCellTwoDices].getColour() == color) {
                        allValidMoves.add(targetCellTwoDices);
                    }
                }
            }
        }
        cantGoPastHouse(x, allValidMoves);
        return allValidMoves;
    }



}


