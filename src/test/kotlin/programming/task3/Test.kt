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
        assertEquals(true, Checker(Color.WHITE, this).isOpposite(Checker(Color.BLACK, this)))
        assertEquals(false, Checker(Color.BLACK, this).isOpposite(Checker(Color.BLACK, this)))
        assertEquals(false, Checker(Color.BLACK, this).isOpposite(Queen(Color.BLACK, this)))
        assertEquals(true, Queen(Color.BLACK, this).isOpposite(Queen(Color.WHITE, this)))
    }

    @Test
    fun winner() {
        assertEquals(null, gameOver())
    }
}