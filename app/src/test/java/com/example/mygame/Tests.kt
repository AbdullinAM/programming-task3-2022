package com.example.mygame

import org.junit.Test

import org.junit.Assert.*

class Test {

    @Test
    fun cellValue() {
        val cell1 = Grid.Cell(0, 1)
        val cell2 = Grid.Cell(1, 3)
        cell1.setValue(4)
        cell2.setValue(6)
        assertEquals(4, cell1.getValue() )
        assertEquals(6, cell2.getValue() )
    }

    @Test
    fun cellNeighbour() {
        val cell1 = Grid.Cell(0, 0)
        cell1.setValue(2)
        val cell2 = Grid.Cell(0, 1)
        cell2.setValue(7)
        val cell3 = Grid.Cell(0, 2)
        cell3.setValue(5)
        val cell4 = Grid.Cell(1, 0)
        cell4.setValue(1)
        val cell5 = Grid.Cell(1, 1)
        cell5.setValue(0)
        val cell6 = Grid.Cell(1, 2)
        cell6.setValue(3)
        val cell7 = Grid.Cell(2, 0)
        cell7.setValue(4)
        val cell8 = Grid.Cell(2, 1)
        cell8.setValue(9)
        val cell9 = Grid.Cell(2, 2)
        cell9.setValue(6)
        val grid = listOf(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9)
        assertEquals(cell3, cell2.getNeighbour(Direction.RIGHT))
        assertEquals(cell9, cell6.getNeighbour(Direction.DOWN))
        assertEquals(cell4, cell5.getNeighbour(Direction.LEFT))
        assertEquals(cell5, cell8.getNeighbour(Direction.UP))
        assertEquals(4, cell8.getNeighbourValue(Direction.LEFT, grid))
        assertEquals(6, cell6.getNeighbourValue(Direction.DOWN, grid))
        assertEquals(5, cell2.getNeighbourValue(Direction.RIGHT, grid) )
        assertEquals(7, cell5.getNeighbourValue(Direction.UP, grid) )
    }

    @Test
    fun cellEquals() {
        val cell1 = Grid.Cell(0, 3)
        val cell2 = Grid.Cell(0, 1)
        val cell3 = Grid.Cell(0, 3)
        assertTrue(cell1 == cell3)
        assertFalse(cell1 == cell2)
    }

    @Test
    fun cellToString() {
        val cell1 = Grid.Cell(3, 0)
        cell1.setValue(8)
        val cell2 = Grid.Cell(1, 4)
        assertEquals("(3, 0) = 8", cell1.toString())
        assertEquals("(1, 4) = null", cell2.toString())
    }

    @Test
    fun createEmptyGrid() {
        assertEquals(16, Grid().createEmptyGrid().size)
    }

    @Test
    fun gridAddValue() {
        val emptyGrid = Grid().createEmptyGrid()
        emptyGrid.forEach { it.setValue(0) }
        val grid = Grid().addValue(emptyGrid)
        val sum = grid.sumOf { it.getValue()!! }
        assertTrue(sum in 2..4)
        assertFalse(sum > 4)
        assertFalse(sum < 2)
    }

}