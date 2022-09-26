package programming.task3;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import programming.task3.Core.Board;
import programming.task3.Core.Cell;
import programming.task3.Core.Checkers;

import java.util.ArrayList;
import java.util.List;


public class boardTests {

    @Test
    public void checkMove(){
        Board board = new Board();

        //проверяем обычный ход
        board.getBoard()[2].setQuantity(1);
        board.getBoard()[2].setColour(Checkers.WHITE);
        board.turns.add(1);
        board.turns.add(2);

        board.move(2,3);
        assertEquals(0, board.getBoard()[2].getQuantity());
        assertEquals(Checkers.NO_COLOR, board.getBoard()[2].getColour());
        assertEquals(1, board.getBoard()[3].getQuantity());
        assertEquals(Checkers.WHITE, board.getBoard()[3].getColour());
        assertEquals(2, board.turns.get(0));
        assertEquals(1, board.turns.size());
        board.turns.clear();

        //проверяем двойной ход и его удаление
        board.getBoard()[6].setQuantity(1);
        board.getBoard()[6].setColour(Checkers.WHITE);
        board.turns.add(1);
        board.turns.add(2);
        board.turns.add(2);

        board.move(6, 9);
        assertEquals(0, board.getBoard()[6].getQuantity());
        assertEquals(Checkers.NO_COLOR, board.getBoard()[6].getColour());
        assertEquals(1, board.getBoard()[9].getQuantity());
        assertEquals(Checkers.WHITE, board.getBoard()[9].getColour());
        assertEquals(1, board.turns.size());
        board.turns.clear();

        //проверяем ход и удаление для хода из клетки с большим индексом в клетку меньшим
        board.getBoard()[23].setQuantity(2);
        board.getBoard()[23].setColour(Checkers.BLACK);
        board.turns.add(5);
        board.turns.add(6);


        board.move(23, 4);
        assertEquals(1, board.getBoard()[23].getQuantity());
        assertEquals(Checkers.BLACK, board.getBoard()[23].getColour());
        assertEquals(1, board.getBoard()[4].getQuantity());
        assertEquals(Checkers.BLACK, board.getBoard()[4].getColour());
        assertEquals(1, board.turns.size());
        board.turns.clear();

        //проверяем удаление двойного хода для хода из клетки с большим индексом в клетку меньшим
        board.turns.add(5);
        board.turns.add(6);
        board.turns.add(6);

        board.move(23, 10);
        assertEquals(0, board.getBoard()[23].getQuantity());
        assertEquals(Checkers.NO_COLOR, board.getBoard()[23].getColour());
        assertEquals(1, board.getBoard()[10].getQuantity());
        assertEquals(Checkers.BLACK, board.getBoard()[10].getColour());
        assertEquals(6, board.turns.get(0));
        assertEquals(1, board.turns.size());
        board.turns.clear();
    }

    @Test
    public void checkAllValidMoves(){
        Board board = new Board();
        List<Integer> allValidMoves = new ArrayList<>();

        //проверка возможности ходить на клетку, занятую оппонентом + двойной ход
        allValidMoves.add(4);
        allValidMoves.add(5);
        board.getBoard()[2].setQuantity(1);
        board.getBoard()[2].setColour(Checkers.WHITE);
        board.getBoard()[3].setQuantity(1);
        board.getBoard()[3].setColour(Checkers.BLACK);
        board.turns.add(1);
        board.turns.add(2);

        assertEquals(allValidMoves, board.allValidMoves(2));
        board.turns.clear();
        allValidMoves.clear();

        //проверка возможности ходить дальше дома для белых
        board.getBoard()[23].setQuantity(1);
        board.getBoard()[23].setColour(Checkers.WHITE);
        board.turns.add(4);
        board.turns.add(6);

        assertEquals(allValidMoves, board.allValidMoves(23));

        //проверка возможности ходить дальше дома для черных
        allValidMoves.add(10);
        board.getBoard()[6].setQuantity(1);
        board.getBoard()[6].setColour(Checkers.BLACK);

        assertEquals(allValidMoves, board.allValidMoves(6));
        allValidMoves.clear();

        //проверка добавления хода из клетки с большим индексом в клетку меньшим
        allValidMoves.add(3);
        allValidMoves.add(5);
        allValidMoves.add(9);
        board.getBoard()[23].setQuantity(1);
        board.getBoard()[23].setColour(Checkers.BLACK);

        assertEquals(allValidMoves, board.allValidMoves(23));
        board.turns.clear();
        allValidMoves.clear();
    }

    @Test
    public void checkOpenExit(){
        Board board = new Board();

        //проверка закрытого выхода
        assertFalse(board.openExit());
        board.turn = Checkers.BLACK;
        assertFalse(board.openExit());

        //проверка открытого выхода
        board.getBoard()[0].setQuantity(0);
        board.getBoard()[0].setColour(Checkers.NO_COLOR);
        board.getBoard()[12].setQuantity(0);
        board.getBoard()[12].setColour(Checkers.NO_COLOR);
        board.getBoard()[23].setQuantity(1);
        board.getBoard()[23].setColour(Checkers.WHITE);
        board.getBoard()[10].setQuantity(1);
        board.getBoard()[10].setColour(Checkers.BLACK);

        board.turn = Checkers.WHITE;
        assertTrue(board.openExit());
        board.turn = Checkers.BLACK;
        assertTrue(board.openExit());

        //выход у белых открыт, у черных закрыт
        board.getBoard()[0].setQuantity(1);
        board.getBoard()[0].setColour(Checkers.BLACK);

        board.turn = Checkers.WHITE;
        assertTrue(board.openExit());
        board.turn = Checkers.BLACK;
        assertFalse(board.openExit());

        //выход у черных открыт, у белых закрыт
        board.getBoard()[0].setQuantity(1);
        board.getBoard()[0].setColour(Checkers.WHITE);

        board.turn = Checkers.WHITE;
        assertFalse(board.openExit());
        board.turn = Checkers.BLACK;
        assertTrue(board.openExit());
    }

    @Test
    public void checkCanExitFromX(){
        Board board = new Board();

        //нельзя выкидывать шашки вне дома
        board.turns.add(2);
        board.turns.add(1);

        assertFalse(board.canExitFromX(0));
        board.turn = Checkers.BLACK;
        assertFalse(board.canExitFromX(12));

        //нельзя выкинуть для белых
        board.getBoard()[23].setQuantity(1);
        board.getBoard()[23].setColour(Checkers.WHITE);
        board.turns.add(2);
        board.turns.add(1);

        board.turn = Checkers.WHITE;
        assertFalse(board.canExitFromX(23));

        //можно выкинуть для белых
        board.getBoard()[0].setQuantity(0);
        board.getBoard()[0].setColour(Checkers.NO_COLOR);

        assertTrue(board.canExitFromX(23));

        //нельзя выкинуть для черных
        board.getBoard()[11].setQuantity(1);
        board.getBoard()[11].setColour(Checkers.BLACK);

        board.turn = Checkers.BLACK;
        assertFalse(board.canExitFromX(11));

        //можно выкинуть для черных
        board.getBoard()[12].setQuantity(0);
        board.getBoard()[12].setColour(Checkers.NO_COLOR);

        assertTrue(board.canExitFromX(11));
    }

    @Test
    public void checkThrowChecker(){
        Board board = new Board();
        board.getBoard()[0].setQuantity(0);
        board.getBoard()[0].setColour(Checkers.NO_COLOR);
        board.getBoard()[12].setQuantity(0);
        board.getBoard()[12].setColour(Checkers.NO_COLOR);

        //выкидывание белых
        board.getBoard()[20].setQuantity(1);
        board.getBoard()[20].setColour(Checkers.WHITE);
        board.turns.add(6);
        board.turns.add(1);

        board.throwChecker(20);
        assertEquals(1, board.turns.get(0));
        assertEquals(1, board.turns.size());
        board.turns.clear();

        //выкидывание черных
        board.getBoard()[6].setQuantity(1);
        board.getBoard()[6].setColour(Checkers.BLACK);
        board.turns.add(6);
        board.turns.add(1);

        board.turn = Checkers.BLACK;
        board.throwChecker(6);
        assertEquals(1, board.turns.get(0));
        assertEquals(1, board.turns.size());
        board.turns.clear();
    }

    @Test
    public void checkWinner(){
        Board board = new Board();
        board.getBoard()[0].setQuantity(0);
        board.getBoard()[0].setColour(Checkers.NO_COLOR);
        board.getBoard()[12].setQuantity(0);
        board.getBoard()[12].setColour(Checkers.NO_COLOR);

        //выграл белый
        board.getBoard()[11].setQuantity(1);
        board.getBoard()[11].setColour(Checkers.BLACK);

        assertEquals(Checkers.WHITE, board.winner());

        //выграл черный
        board.getBoard()[11].setQuantity(0);
        board.getBoard()[11].setColour(Checkers.NO_COLOR);
        board.getBoard()[23].setQuantity(1);
        board.getBoard()[23].setColour(Checkers.WHITE);

        assertEquals(Checkers.BLACK, board.winner());
    }
}
