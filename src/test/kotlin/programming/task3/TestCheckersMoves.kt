package programming.task3

import core.*
import core.Checker.Companion.DIRECTIONS
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestCheckersMoves {

    private var checkers = mutableMapOf<Cell, Checker?>()

    private fun correct(cell: Cell) = cell.x in 0..7 && cell.y in 0..7

    private fun possibleTurnsCheckerTest(cell : Cell): Pair<Boolean, List<Cell>> {
        val result = mutableListOf<Cell>()
        val canEat = mutableListOf<Cell>()
        val colorCheck = checkers[cell]?.color
        if (checkers[cell]!!.color == Color.WHITE) {
            for (i in 0..1) {
                val newCell = cell + DIRECTIONS[i]
                if (checkers[newCell] == null && correct(newCell)) result.add(newCell)
            }
        }
        if (checkers[cell]!!.color == Color.BLACK) {
            for (i in 2..3) {
                val newCell = cell + DIRECTIONS[i]
                if (checkers[newCell] == null && correct(newCell))
                    result.add(newCell)
            }
        }
        for (i in 0..3) {
            val newCell = cell + DIRECTIONS[i]
            val nextCell = newCell + DIRECTIONS[i]
            if (correct(newCell) && correct(nextCell) && checkers[nextCell] == null && checkers[newCell]?.color != colorCheck && checkers[newCell] != null)
                canEat.add(nextCell)
        }
        return if (canEat.isEmpty()) Pair(false , result.toList()) else Pair(true, canEat.toList())
    }

    private fun possibleTurnsQueenTest(cell: Cell): Pair<Boolean, List<Cell>> {
        val result = mutableListOf<Cell>()
        val canEat = mutableListOf<Cell>()
        for (direction in DIRECTIONS) {
            var newCell = cell + direction
            while (checkers[newCell] == null && correct(newCell)) {
                result.add(newCell)
                newCell += direction
            }
        }
        for (direction in DIRECTIONS) {
            var newCell = cell + direction
            while (correct(newCell)) {
                if (checkers[newCell] != null && checkers[newCell]?.color == checkers[cell]!!.color)
                    break
                if (checkers[newCell] != null && checkers[newCell]!!.color != checkers[cell]!!.color) {
                    var nextCell = newCell + direction
                    while (correct(nextCell) && checkers[nextCell] == null) {
                        canEat.add(nextCell)
                        nextCell += direction
                    }
                    break
                }
                newCell += direction
            }
        }
        return if (canEat.isNotEmpty()) Pair(true, canEat.toSet().toList()) else Pair(false, result)
    }

    @Test
    fun checkerMoves() {
        checkers[Cell(3, 4)] = Checker(Color.WHITE)
        checkers[Cell(2, 5)] = null
        checkers[Cell(4, 3)] = Checker(Color.BLACK)
        checkers[Cell(5, 2)] = null
        assertEquals(listOf(Cell(5, 2)), possibleTurnsCheckerTest(Cell(3, 4)).second)
        assertEquals(true, possibleTurnsCheckerTest(Cell(3, 4)).first)
    }


    @Test
    fun queenMoves() {
        checkers = mutableMapOf()
        checkers[Cell(0, 1)] = Queen(Color.WHITE)
        assertEquals(listOf(Cell(1, 0), Cell(1, 2), Cell(2, 3), Cell(3, 4), Cell(4, 5),
            Cell(5, 6), Cell(6, 7), ), possibleTurnsQueenTest(Cell(0, 1)).second)
        checkers[Cell(5, 0)] = Queen(Color.WHITE)
        assertEquals(listOf(Cell(6, 1), Cell(7, 2), Cell(4, 1), Cell(3, 2), Cell(2, 3),
            Cell(1, 4), Cell(0, 5)), possibleTurnsQueenTest(Cell(5, 0)).second)
        assertEquals(false, possibleTurnsQueenTest(Cell(5, 0)).first)
        checkers[Cell(4, 5)] = Checker(Color.BLACK)
        assertEquals(listOf(Cell(5, 6), Cell(6, 7)), possibleTurnsQueenTest(Cell(0, 1)).second)
        assertEquals(true, possibleTurnsQueenTest(Cell(0, 1)).first)
        assertEquals(false, possibleTurnsQueenTest(Cell(5, 0)).first)
        checkers[Cell(4, 1)] = Checker(Color.BLACK)
        checkers[Cell(6, 1)] = Checker(Color.BLACK)
        assertEquals(listOf(Cell(7, 2), Cell(3, 2), Cell(2, 3),
            Cell(1, 4), Cell(0, 5)), possibleTurnsQueenTest(Cell(5, 0)).second)
        assertEquals(true, possibleTurnsQueenTest(Cell(5, 0)).first)
    }
}