package core;

import javafx.util.Pair;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Field {
    private final Cell[][] field;
    private final int[][] minesAround;
    private final int amountMine;
    private final int x;
    private final int y;
    private int totalMarkMines = 0;
    private int correctMarkMines = 0;

    public Field(int x, int y, int amountMine) {
        this.x = x;
        this.y = y;
        this.amountMine = amountMine;
        if (x <= 0 || y <= 0 || x * y < amountMine || amountMine < 0) throw new NumberFormatException();
        this.field = new Cell[x][y];
        this.minesAround = new int[x][y];
        int k = 0;

        //field initialization
        Random random = new Random();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                field[i][j] = new Cell(i, j, false);
            }
        }

        //placement of mines
        int i = 0;
        int j = 0;
        while (k < amountMine) {
            boolean chance = random.nextInt(100) < 5;
            if (chance && getCell(i, j).getState() == Cell.State.Empty) {
                field[i][j].setState(Cell.State.Mine);
                Map<Integer, Pair<Integer, Integer>> neighbours = getNeighbours(i, j);
                for (int counter = 0; counter < 6; counter++) {
                    Pair<Integer, Integer> cords = neighbours.get(counter);
                    if (cords != null) minesAround[cords.getKey()][cords.getValue()]++;
                }
                k++;
            }

            //simulate nested loops
            if (i == x - 1 && j == y - 1) {
                i = -1;
                j = 0;
            }
            if (i == x - 1) {
                j++;
                i = -1;
            }
            if (i + 1 < x) i++;
        }
    }

    //the presence of hidden cells around a given
    public boolean hasHiddenAround(int col, int row) {
        Map<Integer, Pair<Integer, Integer>> neighbours = getNeighbours(col, row);
        for (int i = 0; i < 6; i++) {
            Pair<Integer, Integer> cords = neighbours.get(i);
            if (cords != null && field[cords.getKey()][cords.getValue()].isHidden()) return true;
        }

        return false;
    }

    public int getCol() {
        return x;
    }

    public int getRow() {
        return y;
    }

    public int getAmountMine() {
        return amountMine;
    }

    public int getMineAround(int x, int y) {
        return minesAround[x][y];
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    public void openCell(int x, int y) {
        field[x][y].openCell();
    }

    public int getTotalMarkMines() {
        markCounter();
        return totalMarkMines;
    }

    public int getCorrectMarkMines() {
        markCounter();
        return correctMarkMines;
    }

    //returns a map with the coordinates of neighboring cells
    public Map<Integer, Pair<Integer, Integer>> getNeighbours(int col, int row) {
        Map<Integer, Pair<Integer, Integer>> neighbours = new LinkedHashMap<>();
        boolean correctColLeft = col - 1 >= 0;
        boolean correctColRight = col + 1 < x;
        boolean correctRowLeft = row - 1 >= 0;
        boolean correctRowRight = row + 1 < y;

        if (correctColLeft) neighbours.put(0, new Pair<>(col - 1, row));
        if (correctColRight) neighbours.put(1, new Pair<>(col + 1, row));
        if (correctRowLeft) neighbours.put(2, new Pair<>(col, row - 1));
        if (correctRowRight) neighbours.put(3, new Pair<>(col, row + 1));
        if (row % 2 == 0) {
            if (correctColLeft && correctRowLeft) neighbours.put(4, new Pair<>(col - 1, row - 1));
            if (correctColLeft && correctRowRight) neighbours.put(5, new Pair<>(col - 1, row + 1));
        } else {
            if (correctColRight && correctRowLeft) neighbours.put(4, new Pair<>(col + 1, row - 1));
            if (correctColRight && correctRowRight) neighbours.put(5, new Pair<>(col + 1, row + 1));
        }

        return neighbours;
    }

    //opens cells around the given
    public void openAroundCell(int col, int row) {
        Map<Integer, Pair<Integer, Integer>> neighbours = getNeighbours(col, row);
        for (int i = 0; i < 6; i++) {
            Pair<Integer, Integer> cords = neighbours.get(i);
            if (cords != null) openCell(cords.getKey(), cords.getValue());
        }
    }

    //true -- lose, false -- win
    public boolean checkGameOver() {
        return correctMarkMines != totalMarkMines || totalMarkMines != amountMine;
    }

    public void markCounter() {
        totalMarkMines = 0;
        correctMarkMines = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Cell cell = getCell(i, j);
                if (cell.isMark()) totalMarkMines++;
                if (cell.isMark() && cell.getState() == Cell.State.Mine) correctMarkMines++;
            }
        }
    }
}