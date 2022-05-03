package com.example.mygame

import kotlin.ranges.random

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}


//class Cell(val number: Int) {
//    var v:Int? = null
//    fun setValue(number: Int) {
//        v = number
//    }
//
//    fun getValue(): Int? {
//        return v
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (other !is Cell) return false
//        return this.number == other.number && this.v == other.v
//    }
//}
fun main() {
//    val cell1 = Grid.Cell(0, 0)
//    cell1.setValue(2)
//    val cell2 = Grid.Cell(0, 1)
//    cell2.setValue(2)
//    val cell3 = Grid.Cell(0, 2)
//    cell3.setValue(2)
//    val cell4 = Grid.Cell(1, 0)
//    cell4.setValue(2)
//    val cell5 = Grid.Cell(1, 1)
//    cell5.setValue(0)
//    val cell6 = Grid.Cell(1, 2)
//    cell6.setValue(0)
//    val cell7 = Grid.Cell(2, 0)
//    cell7.setValue(4)
//    val cell8 = Grid.Cell(2, 1)
//    cell8.setValue(4)
//    val cell9 = Grid.Cell(2, 2)
//    cell9.setValue(0)
//    val grid = listOf(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9)
//    val newGrid = Grid().moveGrid(Direction.LEFT, grid)
//    val secondGrid = Grid().moveGrid(Direction.UP, newGrid)
    val grid = Grid().createGrid()
    val gridUP = Grid().moveGrid(Direction.UP, grid)
    val gridDOWN = Grid().moveGrid(Direction.DOWN, gridUP)
    val gridLEFT = Grid().moveGrid(Direction.LEFT, gridDOWN)
    println(gridLEFT)
}

class Grid {
    class Cell(val column: Int, val row: Int) {
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
                Direction.UP -> if (Cell(this.column, this.row - 1) in cells)
                    return cells.find{it == Cell(this.column, this.row - 1)}?.getValue()
                Direction.DOWN -> if (Cell(this.column, this.row + 1) in cells)
                    return cells.find{it == Cell(this.column, this.row + 1)}?.getValue()
                Direction.LEFT -> if (Cell(this.column - 1, this.row) in cells)
                    return cells.find{it == Cell(this.column - 1, this.row)}?.getValue()
                Direction.RIGHT -> if (Cell(this.column + 1, this.row) in cells)
                    return cells.find{it == Cell(this.column + 1, this.row)}?.getValue()

            }
            return null
        }
        fun getNeighbour(direction: Direction): Cell {
            return when(direction) {
                Direction.UP -> Cell(this.column, this.row - 1)
                Direction.DOWN -> Cell(this.column, this.row + 1)
                Direction.LEFT -> Cell(this.column - 1, this.row)
                Direction.RIGHT -> Cell(this.column + 1, this.row)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Cell) return false
            return this.column == other.column && this.row == other.row
        }
        override fun toString(): String {
            return "(${this.column}, ${this.row}) = ${this.getValue()}"
        }
    }


    fun createGrid(): List<Cell> {
//        val cellsValues = mutableListOf<Int?>()
//        val columnsList = mutableListOf<List<Cell>>()
//        val firstCell = Cell((0..3).random(), (0..3).random())
//        var secondCell = Cell((0..15).random(), (0..3).random())
//        while (firstCell != secondCell) secondCell = Cell((0..15).random(), (0..3).random())
//        val firstValue =
//            if ((0..9).random() == 0) 4
//            else 2
//        val secondValue =
//            if ((0..9).random() == 0) 4
//            else 2
//        for (i in 0..15) {
//            if (i == firstCell.number) cellsValues[i] = firstValue
//            if (i == secondCell.number) cellsValues[i] = secondValue
//            else cellsValues[i] = null
//        }
//        return cellsValues.toList()
//        for (column in 0..3) {
//            val cellList = mutableListOf<Cell>()
//            for (row in  0..3) {
//                if (column == firstCell.column && row == firstCell.row)
//            }
//        }
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
        //Column, row version
        /*val columnsList = mutableListOf<List<Cell>>()
        for (column in 0..3) {
            val cellList = mutableListOf<Cell>()
            for (row in 0..3) {
                cellList.add(Cell(column, row))
            }
            columnsList.add(cellList)
        }
        return columnsList

         */
        val result = mutableListOf<Cell>()
        for (column in 0..3) {
            for (row in 0..3) {
                result.add(Cell(column, row))
            }
        }
        return result
    }
// Create check line(row, column) with empty cells function or try while cycle
    fun moveGrid(direction: Direction, grid: List<Cell>): List<Cell> {
        val gridNew = grid.toMutableList()
        grid.forEach {
                var cell = it
                var cellValue = it.getValue()
                var neighbour = it.getNeighbour(direction)
                var neighbourValue = it.getNeighbourValue(direction, grid)
                var sum = neighbourValue?.plus(cellValue!!)
//                when (neighbourValue) {
//                    cellValue -> {
//                        gridNew.find { it == neighbour }?.setValue(sum)
//                        gridNew.find { it == cell}?.setValue(0)
//                    }
//                    0 -> {
//                        val neighbourNext = neighbour.getNeighbour(direction)
//                        gridNew.find { it == neighbour }?.setValue(it.getValue())
//                        gridNew.find { it == cell}?.setValue(0)
//                    }
            while (neighbourValue != null) {
                when (neighbourValue) {
                    cellValue -> {
                        gridNew.find { it == neighbour }?.setValue(sum)
                        gridNew.find { it == cell }?.setValue(0)
                        break
                    }
                    0 -> {
                        gridNew.find { it == neighbour }?.setValue(cellValue)
                        gridNew.find { it == cell }?.setValue(0)
                        cell = neighbour
                        cellValue = neighbourValue
                        neighbour = neighbour.getNeighbour(direction)
                        neighbourValue = it.getNeighbourValue(direction, grid)
                        sum = neighbourValue?.plus(cellValue)
                    }
                    else -> break
                }
                }
        }
        return gridNew
    }

}