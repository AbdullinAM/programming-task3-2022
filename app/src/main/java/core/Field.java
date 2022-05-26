package core;
import java.util.Random;

public class Field {
    private final Cell[][] field;
    private final int[][] minesAround;
    private final int amountMine;
    private final int x;
    private final int y;
    public Field(int x, int y, int amountMine) {
        this.x = x;
        this.y = y;
        this.amountMine = amountMine;
        this.field = new Cell[x][y];
        this.minesAround = new int[x][y];
        if (x == 0 || y == 0 || x * y < amountMine) throw new NumberFormatException();
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
                    boolean correctColLeft = i - 1 >= 0;
                    boolean correctColRight = i + 1 < x;
                    boolean correctRowLeft = j - 1 >= 0;
                    boolean correctRowRight = j + 1 < y;
                    field[i][j].setState(Cell.State.Mine);
                    if (correctColLeft) minesAround[i - 1][j]++;
                    if (correctColRight) minesAround[i + 1][j]++;
                    if (correctRowLeft) minesAround[i][j - 1]++;
                    if (correctRowRight) minesAround[i][j + 1]++;
                    if (j % 2 == 0) {
                        if (correctColLeft && correctRowLeft) minesAround[i - 1][j - 1]++;
                        if (correctColLeft && correctRowRight) minesAround[i - 1][j + 1]++;

                    } else {
                        if (correctColRight && correctRowLeft) minesAround[i + 1][j - 1]++;
                        if (correctColRight && correctRowRight) minesAround[i + 1][j + 1]++;
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
                    i = - 1;
                }
                if (i + 1 < x) i++;
        }
    }

    //the presence of hidden cells around a given
    public boolean hasHiddenAround(int col, int row) {
            boolean correctColLeft = col - 1 >= 0;
            boolean correctColRight = col + 1 < x;
            boolean correctRowLeft = row - 1 >= 0;
            boolean correctRowRight = row + 1 < y;
            if (correctColLeft && field[col - 1][row].isHidden() ) return true;
            if (correctColRight && field[col + 1][row].isHidden() ) return true;
            if (correctRowLeft && field[col][row - 1].isHidden()) return true;
            if (correctRowRight && field[col][row + 1].isHidden()) return true;
            if (row % 2 == 0) {
                if (correctRowLeft && correctColLeft && field[col - 1][row - 1].isHidden()) return true;
                if (correctColLeft && correctRowRight && field[col - 1][row + 1].isHidden()) return true;
            } else {
                if (correctColRight && field[col + 1][row - 1].isHidden()) return true;
                if (correctColRight && correctRowRight && field[col + 1][row + 1].isHidden()) return true;
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

    public void openCell(int x, int y) {
        field[x][y].openCell();
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    //opens cells around the given
    public void openAroundCell(int col, int row) {
        if (col - 1 >= 0) openCell(col - 1, row);
        if (col + 1 < x) openCell(col + 1, row);
        if (row - 1 >= 0) openCell(col, row - 1);
        if (row + 1 < y) openCell(col, row + 1);
        if (row % 2 == 0) {
            if (col - 1 >= 0 && row - 1 >= 0) openCell(col - 1, row - 1);
            if (col - 1 >= 0 && row + 1 < y) openCell(col - 1, row + 1);
        } else {
            if (col + 1 < x && row - 1 >= 0) openCell(col + 1, row - 1);
            if (col + 1 < x && row + 1 < y) openCell(col + 1, row + 1);
        }
    }
}