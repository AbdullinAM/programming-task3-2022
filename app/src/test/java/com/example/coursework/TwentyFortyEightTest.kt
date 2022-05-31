package com.example.coursework

import com.example.coursework.core.Cell
import com.example.coursework.core.Directions
import com.example.coursework.logic.Game
import org.junit.Test

import org.junit.Assert.*


class TwentyFortyEightTest {

    @Test
    fun getEmptyCellsTest() {
        val game = Game()
        val gameGrid = game.getGrid()
        val result = mutableListOf<Cell>()
        gameGrid.forEach { (cell, value) ->
            if (value == null) result.add(cell)
        }
        val expected = game.getEmptyCells()

        assertEquals(expected, result)
    }

    @Test
    fun spawnCellTest() {
        val game = Game()
        val gameGrid = game.getGrid()
        val startEmptyCells = game.getEmptyCells().toMutableList()
        val spawnedCell = game.spawnCell()
        gameGrid[spawnedCell!!.first] = spawnedCell.second
        val expected = game.getEmptyCells()
        startEmptyCells.remove(spawnedCell.first)

        assertEquals(expected, startEmptyCells)
    }

    @Test
    fun isInGridTest() {
        val game = Game()
        assertTrue(game.isInGrid(Cell(3, 0)))
        assertTrue(game.isInGrid(Cell(0, 0)))
        assertTrue(game.isInGrid(Cell(0, 3)))
        assertFalse(game.isInGrid(Cell(4, 1)))
        assertFalse(game.isInGrid(Cell(4, 4)))
    }

    @Test
    // Угловая клетка
    fun getClosestCellTest1() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
        }
        val cell1 = Cell(0, 0)
        assertEquals(Cell(0, 0), game.getClosestCell(Directions.UP, cell1))
        assertEquals(Cell(3, 0), game.getClosestCell(Directions.RIGHT, cell1))
        assertEquals(Cell(0, 3), game.getClosestCell(Directions.DOWN, cell1))
        assertEquals(Cell(0, 0), game.getClosestCell(Directions.LEFT, cell1))
    }

    @Test
    // Ситуация с препятствиями со всех сторон
    fun getClosestCellTest2() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
        }
        val movingCell = Cell(1, 2)
        gameGrid[movingCell] = 2
        val upperCell = Cell(1, 0)
        gameGrid[upperCell] = 2
        val leftCell = Cell(0, 2)
        gameGrid[leftCell] = 2
        val rightCell = Cell(3, 2)
        gameGrid[rightCell] = 2
        val lowerCell = Cell(1, 3)
        gameGrid[leftCell] = 2

        assertEquals(upperCell, game.getClosestCell(Directions.UP, movingCell))
        assertEquals(rightCell, game.getClosestCell(Directions.RIGHT, movingCell))
        assertEquals(lowerCell, game.getClosestCell(Directions.DOWN, movingCell))
        assertEquals(leftCell, game.getClosestCell(Directions.LEFT, movingCell))
    }

    @Test
    // Ситуация с одной клеткой на поле (результат - список крайних свободных клеток)
    fun getClosestCellsTest1() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
        }

        val movingCell = Cell(2, 2)
        gameGrid[movingCell] = 2

        val expected = listOf(Cell(2, 0), Cell(2, 3),
            Cell(0, 2), Cell(3, 2))
        val result = game.getClosestCells(movingCell)

        assertEquals(expected, result)
    }

    @Test
    // Ситуация с двумя препятствиями (слева и сверху), справа и снизу спускается до последней клетки
    fun getClosestCellsTest2() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
        }
        val movingCell = Cell(2, 1)
        gameGrid[movingCell] = 2

        val leftCell = Cell(1, 1)
        gameGrid[leftCell] = 2

        val upperCell = Cell(2, 0)
        gameGrid[upperCell] = 2

        val expected = listOf(Cell(2, 0), Cell(2, 3),
            Cell(1, 1), Cell(3, 1))
        val result = game.getClosestCells(movingCell)

        assertEquals(expected, result)
    }

    @Test
    // Сложение четырёх двоек в столбце
    fun moveGridColumnTest() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
            if (cell in setOf(Cell(0,0), Cell(0,1),
                    Cell(0,2), Cell(0, 3))) {
                gameGrid[cell] = 2
            }
        }
        game.moveGrid(Directions.UP)

        val expected = listOf(Cell(0,0) to 4, Cell(0,1) to 4)
        val result = mutableListOf<Pair<Cell, Int?>>()
        var notNullCounter = 0
        for (gridCell in gameGrid.toList()) {
            if (gridCell.second != null) notNullCounter += 1
            if (gridCell.first in setOf(Cell(0,0), Cell(0, 1))) {
                result.add(gridCell)
            }
        }

        assertEquals(expected, result)
        assertEquals(3, notNullCounter)
    }

    @Test
    // Сложение четырёх двоек, расположенных по углам
    fun moveGridCornersTest() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
            if (cell in setOf(Cell(0,0), Cell(3,0),
                    Cell(0,3), Cell(3, 3))) {
                gameGrid[cell] = 2
            }
        }
        game.moveGrid(Directions.LEFT)

        val expected = listOf(Cell(0,0) to 4, Cell(0,3) to 4)
        val result = mutableListOf<Pair<Cell, Int?>>()
        var notNullCounter = 0
        for (gridCell in gameGrid.toList()) {
            if (gridCell.second != null) notNullCounter += 1
            if (gridCell.first in setOf(Cell(0,0), Cell(0, 3))) {
                result.add(gridCell)
            }
        }

        assertEquals(expected, result)
        assertEquals(3, notNullCounter)
    }

    @Test
    // Перемещение клеток до препятствия (неравное значение)
    fun moveGridValuesTest() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
        }
        gameGrid[Cell(0, 0)] = 4
        gameGrid[Cell(0, 3)] = 4
        gameGrid[Cell(2,0)] = 2
        gameGrid[Cell(3, 3)] = 2
        game.moveGrid(Directions.LEFT)

        val expected = listOf(Cell(0,0) to 4, Cell(1, 0) to 2,
            Cell(0,3) to 4, Cell(1, 3) to 2)

        val result = mutableListOf<Pair<Cell, Int?>>()
        var notNullCounter = 0
        for (gridCell in gameGrid.toList()) {
            if (gridCell.second != null) notNullCounter += 1
            if (gridCell.first in setOf(Cell(0,0), Cell(0, 3),
                Cell(1, 0), Cell(1,3))) {
                result.add(gridCell)
            }
        }

        assertEquals(expected, result)
        assertEquals(5, notNullCounter)
    }

    @Test
    // Новые клетки не появляются на доске, если двигать нечего
    fun moveGridSpawnTest() {
        val game = Game()
        val gameGrid = game.getGrid()
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
        }
        gameGrid[Cell(0, 1)] = 2
        gameGrid[Cell(1, 0)] = 2
        gameGrid[Cell(2,0)] = 2
        gameGrid[Cell(3, 0)] = 2

        val expected = gameGrid
        game.moveGrid(Directions.UP)

        assertEquals(expected, gameGrid)
    }

    @Test
    // Проверка, что при невозможности походить возникает game over
    fun gameOverTest() {
        val game = Game()
        val gameGrid = game.getGrid()

        var cellValue = 2
        gameGrid.forEach { ( cell, value ) ->
            if (value != null) gameGrid[cell] = null
            gameGrid[cell] = cellValue
            cellValue *= 2
        }

        game.checkGameOver()
        assertTrue(game.getGameOver())
    }
}