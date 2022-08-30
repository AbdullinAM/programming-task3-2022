package programming.task3.Core;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {


    private Checkers turn = Checkers.WHITE;

    private BoardListener listener = null;

    private final Cell[] board = new Cell[24];

    private final List<Integer> turns = new ArrayList<>();


    Dice dice = new Dice();



    public Cell[] getBoard(){return board;}

    public void registerListener(BoardListener listener) {
        this.listener = listener;
    }

    public Checkers getTurn() {
        return turn;
    }

    public Board(){
        for (int i = 0; i < 24; i++) {board[i] = new Cell();}
        board[0].setColour(Checkers.WHITE);
        board[0].setQuantity(15);
        board[12].setColour(Checkers.BLACK);
        board[12].setQuantity(15);
    }

    public  void setBoardListener(BoardListener l){
        listener = l;
    }




    public void throwDices(){
        if (turns.isEmpty()){
            turns.add(dice.throwDice());
            turns.add(dice.throwDice());
            if (Objects.equals(turns.get(0), turns.get(1))){
                turns.add(turns.get(0));
                turns.add(turns.get(0));
            }
        }
    }

    // add player to play white method ;)

    public void move (int x, int finalPos){
        if (board[finalPos].getQuantity() == 0){
            board[finalPos].setColour(board[x].getColour());
        }
        board[finalPos].setQuantity(board[finalPos].getQuantity() + 1);

        board[x].setQuantity(board[x].getQuantity() - 1);
        if (board[x].getQuantity() == 0){
            board[x].setColour(Checkers.NO_COLOR);
        }
    }

    public List<Integer> allValidMoves(int x) {
        List<Integer> allValidMoves = new ArrayList<>();
        Checkers color = board[x].getColour();

        for (Integer t : turns){
            int targetCell;

            if (t + x > 24){
                targetCell = (t + x) % 24;
            } else {
                targetCell = t + x;
            }

            if(!board[targetCell].isOccupied() || board[targetCell].getColour() == color) {
                allValidMoves.add(targetCell);
            }
        }


        return allValidMoves;
    }



}


