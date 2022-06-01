package main;
import core.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    void testCellState() {
        Cell cell = new Cell(2,5, true);
        assertSame(Cell.State.Mine, cell.getState());
        cell.clickResult("PRIMARY");
        assertSame(Cell.State.MineExploded, cell.getState());

        cell.setState(Cell.State.Empty);
        assertSame(Cell.State.Empty, cell.getState());
        cell.clickResult("PRIMARY");
        assertSame(Cell.State.Empty, cell.getState());
    }

    @Test
    void testCellCords() {
        Cell cell = new Cell(5,5, true);
        assertSame(5, cell.getX());
        assertSame(5, cell.getY());

        cell = new Cell(2,1, true);
        assertSame(2, cell.getX());
        assertSame(1, cell.getY());

        cell = new Cell(13,24, false);
        assertSame(13, cell.getX());
        assertSame(24, cell.getY());

        cell = new Cell(3,2, true);
        assertNotSame(99, cell.getX());
        assertNotSame(69, cell.getY());

        cell = new Cell(1,24, false);
        assertNotSame(13, cell.getX());
        assertSame(24, cell.getY());
    }

    @Test
    void testCellOpen() {
        Cell cell = new Cell(5,5, true);
        assertTrue(cell.isHidden());
        cell.openCell();
        assertFalse(cell.isHidden());

        cell = new Cell(5,5, false);
        assertTrue(cell.isHidden());
        cell.openCell();
        assertFalse(cell.isHidden());
    }

    @Test
    void testCellClick() {
        Cell cell = new Cell(5, 5, true); //mine cell
        assertTrue(cell.isHidden()); //hidden
        assertFalse(cell.isMark()); //not flagged
        assertSame(Cell.ClickResult.Default, cell.clickResult("SECONDARY"));
        assertTrue(cell.isMark()); //flagged
        assertSame(Cell.ClickResult.Default, cell.clickResult("PRIMARY")); //can't explode, because it  was flagged
        assertSame(Cell.ClickResult.Default, cell.clickResult("SECONDARY"));
        assertFalse(cell.isMark()); //unflagged
        assertSame(Cell.ClickResult.Explode, cell.clickResult("PRIMARY")); //explode

        cell = new Cell(2, 0, false); //empty cell
        assertTrue(cell.isHidden());
        assertFalse(cell.isMark());
        assertSame(Cell.ClickResult.Open, cell.clickResult("PRIMARY"));
        assertFalse(cell.isHidden());
    }
}
