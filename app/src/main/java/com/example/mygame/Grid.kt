package com.example.mygame

import kotlin.ranges.random

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun main() {
    val cell1 = Grid.Cell(0, 0)
    cell1.setValue(2)
    val cell2 = Grid.Cell(0, 1)
    cell2.setValue(2)
    val cell3 = Grid.Cell(0, 2)
    cell3.setValue(2)
    val cell4 = Grid.Cell(1, 0)
    cell4.setValue(2)
    val cell5 = Grid.Cell(1, 1)
    cell5.setValue(0)
    val cell6 = Grid.Cell(1, 2)
    cell6.setValue(0)
    val cell7 = Grid.Cell(2, 0)
    cell7.setValue(4)
    val cell8 = Grid.Cell(2, 1)
    cell8.setValue(4)
    val cell9 = Grid.Cell(2, 2)
    cell9.setValue(0)
    val grid = listOf(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9)
    println(cell6.getNeighbourValue(Direction.DOWN,grid))
//    val newGrid = Grid().moveGrid(Direction.LEFT, grid)
//    val secondGrid = Grid().moveGrid(Direction.UP, newGrid)
}

class Grid {
    class Cell(val row: Int, val column: Int) {
        var v:Int? = null
        fun setValue(number: Int?) {
            v = number
        }

        fun getValue(): Int? {
            return v
        }

        fun getNeighbourValue(direction: Direction, grid: List<Cell>): Int? {
            val cells = mutableListOf<Cell>()
            grid.forEach {  cells.add(it) }
            when(direction) {
                Direction.UP -> if (Cell(this.row - 1, this.column) in cells)
                    return cells.find{it == Cell(this.row - 1, this.column)}?.getValue()
                Direction.DOWN -> if (Cell(this.row + 1, this.column) in cells)
                    return cells.find{it == Cell(this.row + 1, this.column)}?.getValue()
                Direction.LEFT -> if (Cell(this.row, this.column - 1,) in cells)
                    return cells.find{it == Cell(this.row, this.column - 1)}?.getValue()
                Direction.RIGHT -> if (Cell(this.row, this.column + 1) in cells)
                    return cells.find{it == Cell(this.row, this.column + 1)}?.getValue()

            }
            return null
        }
        fun getNeighbour(direction: Direction): Cell {
            return when(direction) {
                Direction.UP -> Cell(this.row - 1, this.column)
                Direction.DOWN -> Cell(this.row + 1, this.column)
                Direction.LEFT -> Cell(this.row, this.column - 1)
                Direction.RIGHT -> Cell(this.row, this.column + 1)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Cell) return false
            return this.column == other.column && this.row == other.row
        }
        override fun toString(): String {
            return "(${this.row}, ${this.column}) = ${this.getValue()}"
        }
    }


    fun createGrid(): List<Cell> {
        val grid = createEmptyGrid()
        val firstCell = Cell((0..3).random(), (0..3).random())
        var secondCell = Cell((0..3).random(), (0..3).random())
        while (firstCell == secondCell) secondCell = Cell((0..3).random(), (0..3).random())
        val firstValue =
            if ((0..9).random() > 0) 2
            else 4
        val secondValue =
            if ((0..9).random() > 0) 2
            else 4
        grid.forEach {
                when(it) {
                    firstCell -> it.setValue(firstValue)
                    secondCell -> it.setValue(secondValue)
                    else -> it.setValue(0)
                }
        }
        return grid
    }


    fun createEmptyGrid(): List<Cell> {
        val result = mutableListOf<Cell>()
        for (row in 0..3) {
            for (column in 0..3) {
                result.add(Cell(row, column))
            }
        }
        return result
    }

    fun addValue(grid: List<Cell>): List<Cell> {
        val result = grid.toMutableList()
        val emptyCells = result.filter { it.getValue() == 0 }
        //java.util.NoSuchElementException: Cannot get random in empty range: 0..-1
        val position = (emptyCells.indices).random()
        val value =
            if ((0..9).random() > 0) 2
            else 4
        result.find { it == emptyCells[position]}?.setValue(value)
        return result
    }

    fun moveRight(grid: List<Cell>): List<Cell> {
//        val grid = grid.reversed().toMutableList()
//        grid.forEach {
//            var cell = it
//            var cellValue = it.getValue()
//            var neighbour = it.getNeighbour(Direction.RIGHT)
//            var neighbourValue = it.getNeighbourValue(Direction.RIGHT, grid)
//            var sum = neighbourValue?.plus(cellValue!!)
//            while (neighbourValue != null) {
//                when (neighbourValue) {
//                    cellValue -> {
//                        grid.find { it == neighbour }?.setValue(sum)
//                        grid.find { it == cell }?.setValue(0)
//                        break
//                    }
//                    0 -> {
//                        grid.find { it == neighbour }?.setValue(cellValue)
//                        grid.find { it == cell }?.setValue(0)
//                        cell = neighbour
//                        cellValue = neighbourValue
//                        neighbour = neighbour.getNeighbour(Direction.RIGHT)
//                        neighbourValue = cell.getNeighbourValue(Direction.RIGHT, grid)
//                        sum = neighbourValue?.plus(cellValue)
//                    }
//                    else -> break
//                }
//            }
//        }
//        return grid.reversed()
        val grid = grid.toMutableList()
        for (i in 15 downTo 0) {
            var cell = grid[i]
            var cellValue = cell.getValue()
            if (cellValue == 0) continue
            var neighbour = cell.getNeighbour(Direction.RIGHT)
            var neighbourValue = cell.getNeighbourValue(Direction.RIGHT, grid)
            var sum = neighbourValue?.plus(cellValue!!)
            while (neighbourValue != null) {
                when (neighbourValue) {
                    cellValue -> {
                        grid.find { it == neighbour }?.setValue(sum)
                        grid.find { it == cell }?.setValue(0)
                        break
                    }
                    0 -> {
                        grid.find { it == neighbour }?.setValue(cellValue)
                        grid.find { it == cell }?.setValue(0)
                        cell = neighbour
                        cellValue = neighbourValue
                        neighbour = neighbour.getNeighbour(Direction.RIGHT)
                        neighbourValue = cell.getNeighbourValue(Direction.RIGHT, grid)
                        sum = neighbourValue?.plus(cellValue)
                    }
                    else -> break
                }
            }
        }
        return grid
    }

    fun moveLeft(grid: List<Cell>): List<Cell> {
        val grid = grid.toMutableList()
        grid.forEach {
            var cell = it
            var cellValue = it.getValue()
            var neighbour = it.getNeighbour(Direction.LEFT)
            var neighbourValue = it.getNeighbourValue(Direction.LEFT, grid)
            var sum = neighbourValue?.plus(cellValue!!)
            while (neighbourValue != null) {
                when (neighbourValue) {
                    cellValue -> {
                        grid.find { it == neighbour }?.setValue(sum)
                        grid.find { it == cell }?.setValue(0)
                        break
                    }
                    0 -> {
                        grid.find { it == neighbour }?.setValue(cellValue)
                        grid.find { it == cell }?.setValue(0)
                        cell = neighbour
                        cellValue = neighbourValue
                        neighbour = neighbour.getNeighbour(Direction.LEFT)
                        neighbourValue = cell.getNeighbourValue(Direction.LEFT, grid)
                        sum = neighbourValue?.plus(cellValue)
                    }
                    else -> break
                }
            }
        }
        return grid
    }

    fun moveDown(grid: List<Cell>): List<Cell> {
        val grid = grid.reversed().toMutableList()
        grid.forEach {
            var cell = it
            var cellValue = it.getValue()
            var neighbour = it.getNeighbour(Direction.DOWN)
            var neighbourValue = it.getNeighbourValue(Direction.DOWN, grid)
            var sum = neighbourValue?.plus(cellValue!!)
            while (neighbourValue != null) {
                when (neighbourValue) {
                    cellValue -> {
                        grid.find { it == neighbour }?.setValue(sum)
                        grid.find { it == cell }?.setValue(0)
                        break
                    }
                    0 -> {
                        grid.find { it == neighbour }?.setValue(cellValue)
                        grid.find { it == cell }?.setValue(0)
                        cell = neighbour
                        cellValue = neighbourValue
                        neighbour = neighbour.getNeighbour(Direction.DOWN)
                        neighbourValue = cell.getNeighbourValue(Direction.DOWN, grid)
                        sum = neighbourValue?.plus(cellValue)
                    }
                    else -> break
                }
            }
        }
        return grid.reversed()
    }

    fun moveUp(grid: List<Cell>): List<Cell> {
        val grid = grid.toMutableList()
        grid.forEach {
            var cell = it
            var cellValue = it.getValue()
            var neighbour = it.getNeighbour(Direction.UP)
            var neighbourValue = it.getNeighbourValue(Direction.UP, grid)
            var sum = neighbourValue?.plus(cellValue!!)
            while (neighbourValue != null) {
                when (neighbourValue) {
                    cellValue -> {
                        grid.find { it == neighbour }?.setValue(sum)
                        grid.find { it == cell }?.setValue(0)
                        break
                    }
                    0 -> {
                        grid.find { it == neighbour }?.setValue(cellValue)
                        grid.find { it == cell }?.setValue(0)
                        cell = neighbour
                        cellValue = neighbourValue
                        neighbour = neighbour.getNeighbour(Direction.UP)
                        neighbourValue = cell.getNeighbourValue(Direction.UP, grid)
                        sum = neighbourValue?.plus(cellValue)
                    }
                    else -> break
                }
            }
        }
        return grid
    }

//    fun moveGrid(direction: Direction, grid: List<Cell>): List<Cell>{
//        return when(direction) {
//            Direction.UP -> moveUp(grid)
//            Direction.DOWN -> moveDown(grid)
//            Direction.RIGHT -> moveRight(grid)
//            Direction.LEFT -> moveLeft(grid)
//        }
//    }
//// put foEach code in function  cellValue == 0 continue, index + - 4 + - 1 realisation
    fun moveGrid(direction: Direction, grid: List<Cell>): List<Cell> {
        var gridResult =  listOf<Cell>()
        val gridMain = grid.toMutableList()
        val gridReversed = grid.reversed().toMutableList()
        if (direction == Direction.DOWN || direction == Direction.RIGHT) {
            gridMain.forEach {
                var cell = it
                var cellValue = it.getValue()
                var neighbour = it.getNeighbour(direction)
                var neighbourValue = it.getNeighbourValue(direction, grid)
                var sum = neighbourValue?.plus(cellValue!!)
                while (neighbourValue != null) {
                    when (neighbourValue) {
                        cellValue -> {
                            gridMain.find { it == neighbour }?.setValue(sum)
                            gridMain.find { it == cell }?.setValue(0)
                            break
                        }
                        0 -> {
                            gridMain.find { it == neighbour }?.setValue(cellValue)
                            gridMain.find { it == cell }?.setValue(0)
                            cell = neighbour
                            cellValue = neighbourValue
                            neighbour = neighbour.getNeighbour(direction)
                            neighbourValue = it.getNeighbourValue(direction, grid)
                            sum = neighbourValue?.plus(cellValue)
                        }
                        else -> break
//                        else ->
//                            {
//                                val nextNeighbour = neighbour.getNeighbour(direction)
//                                val nextNeighbourValue = nextNeighbour.getValue()
//                                val sumNext = nextNeighbourValue?.plus(neighbourValue)
//                                neighbourValue = when(nextNeighbourValue) {
//                                    neighbourValue -> {
//                                        gridMain.find { it == nextNeighbour }?.setValue(sumNext)
//                                        gridMain.find { it == neighbour }?.setValue(0)
//                                        0
//                                    }
//                                    0 -> {
//                                        gridMain.find { it == nextNeighbour }?.setValue(neighbourValue)
//                                        gridMain.find { it == neighbour }?.setValue(0)
//                                        0
//                                    }
//                                    else -> break
//                                }
//                            }

                    }
                }
            }
            gridResult = gridMain
        }
        else {
            gridReversed.forEach {
                var cell = it
                var cellValue = it.getValue()
                var neighbour = it.getNeighbour(direction)
                var neighbourValue = it.getNeighbourValue(direction, grid)
                var sum = neighbourValue?.plus(cellValue!!)
                while (neighbourValue != null) {
                    when (neighbourValue) {
                        cellValue -> {
                            gridMain.find { it == neighbour }?.setValue(sum)
                            gridMain.find { it == cell }?.setValue(0)
                            break
                        }
                        0 -> {
                            gridMain.find { it == neighbour }?.setValue(cellValue)
                            gridMain.find { it == cell }?.setValue(0)
                            cell = neighbour
                            cellValue = neighbourValue
                            neighbour = neighbour.getNeighbour(direction)
                            neighbourValue = it.getNeighbourValue(direction, grid)
                            sum = neighbourValue?.plus(cellValue!!)
                        }
                        else -> break
//                        else ->
//                            {
//                                val nextNeighbour = neighbour.getNeighbour(direction)
//                                val nextNeighbourValue = nextNeighbour.getValue()
//                                val sumNext = nextNeighbourValue?.plus(neighbourValue!!)
//                                neighbourValue = when(nextNeighbourValue) {
//                                    neighbourValue -> {
//                                        gridMain.find { it == nextNeighbour }?.setValue(sumNext)
//                                        gridMain.find { it == neighbour }?.setValue(0)
//                                        0
//                                    }
//                                    0 -> {
//                                        gridMain.find { it == nextNeighbour }?.setValue(neighbourValue)
//                                        gridMain.find { it == neighbour }?.setValue(0)
//                                        0
//                                    }
//                                    else -> break
//                                }
//                            }
                    }
                }
            }
            gridResult = gridReversed.reversed()
        }
        gridResult = addValue(gridResult)
        return gridResult
    }

}