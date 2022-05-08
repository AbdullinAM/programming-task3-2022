package programming.task3

import core.*
import junit.framework.TestCase.*
import org.junit.Test

class Test: Board() {
    @Test
    fun movesChecker() {
        assertEquals(listOf(Cell(2, 3), Cell(0, 3)), possibleTurns(Cell(1, 2)).second)
        assertEquals(listOf(Cell(1, 4)), possibleTurns(Cell(0, 5)).second)
        assertEquals(listOf(Cell(3, 4), Cell(1, 4)), possibleTurns(Cell(2, 5)).second)
        assertEquals(false, possibleTurns(Cell(1, 2)).first)
    }

    @Test
    fun testOpposite() {
        assertEquals(true, Checker(Color.WHITE).isOpposite(Checker(Color.BLACK)))
        assertEquals(false, Checker(Color.BLACK).isOpposite(Checker(Color.BLACK)))
        assertEquals(false, Checker(Color.BLACK).isOpposite(Queen(Color.BLACK)))
        assertEquals(true, Queen(Color.BLACK).isOpposite(Queen(Color.WHITE)))
    }

    @Test
    fun winner() {
        assertEquals(null, gameOver())
    }
}