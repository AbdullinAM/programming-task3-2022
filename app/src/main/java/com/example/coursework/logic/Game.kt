package com.example.coursework.logic

import androidx.compose.ui.graphics.Color
import com.example.coursework.core.Cell
import com.example.coursework.core.Directions
import com.example.coursework.core.Moves
import com.example.coursework.core.Vector
import org.jetbrains.annotations.NotNull
import kotlin.random.Random


private var grid = mutableMapOf<Cell, Int?>()
const val FIELD_SIZE = 4

fun moveGrid(directions: Directions): MutableMap<Cell, Int?> {
    var wasReversed = false
    if (directions == Directions.DOWN || directions == Directions.RIGHT) {
        wasReversed = true
        grid = reverseGrid()
    }

    grid.forEach { (cell, value) ->
        if (value != null) {
            val allowedNeighbor = getClosestCell(directions, cell)
            if (grid[allowedNeighbor] == null) {
                grid[allowedNeighbor] = value
                grid[cell] = null
            } else if (grid[allowedNeighbor] == value) {
                if (allowedNeighbor != cell) {
                    grid[allowedNeighbor] = grid[allowedNeighbor]?.times(2)
                    grid[cell] = null
                }
            } else {
                grid[allowedNeighbor.moveForward(getOpposite(directions).getVector())] = value
                if (allowedNeighbor.moveForward(getOpposite(directions).getVector()) != cell) {
                    grid[cell] = null
                }
            }
        }
    }

    if (wasReversed) grid = reverseGrid()
    val newCell = spawnCell()
    grid[newCell!!.first] = newCell.second

    return grid
}

fun getOpposite(directions: Directions): Directions {
    return when (directions) {
        Directions.UP -> Directions.DOWN
        Directions.DOWN -> Directions.UP
        Directions.RIGHT -> Directions.LEFT
        else -> Directions.RIGHT
    }
}
fun reverseGrid(): MutableMap<Cell, Int?> {
    val result = mutableMapOf<Cell, Int?>()
    val mapToList = grid.toList().reversed()
    for (i in mapToList.indices) {
        result[mapToList[i].first] = mapToList[i].second
    }
    return result
}

fun main() {
    val grid = createEmptyGrid()
    val cell1 = Cell(0,0)
    val cell2 = Cell(0,1)
    val cell3 = Cell(0,3)
    grid[cell1] = 4
    grid[cell2] = 4
    grid[cell3] = 2
    /*val spawn2 = spawnCell()
    val spawn3 = spawnCell()
    val spawn4 = spawnCell()
    val spawn5 = spawnCell()
    val spawn6 = spawnCell()
    print(spawn1)
    print(spawn2)
    print(spawn3)
    print(spawn4)
    print(spawn5)
    print(spawn6) */
    print(grid)
    print(moveGrid(Directions.UP))
}

// Ищет место, ближайшее к NotNull-клетке
fun getClosestCell(directions: Directions, cell: Cell): Cell {
    val vector = directions.getVector()
    var result = cell
    var nextCell = cell.moveForward(vector)
    while (grid[nextCell] == null && isInGrid(nextCell)) {
        result = nextCell
        nextCell = nextCell.moveForward(vector)
    }

    if (grid[result.moveForward(vector)] != null) {
        result = result.moveForward(vector)
    }
    return result
}

fun isInGrid(cell: Cell): Boolean = cell.row < FIELD_SIZE && cell.column < FIELD_SIZE
        && cell.row >= 0 && cell.column >= 0

fun createStartGrid(): MutableMap<Cell, Int?> {
    val startGrid = createEmptyGrid()
    val firstCell = spawnCell()
    val secondCell = spawnCell()
    grid[firstCell!!.first] = firstCell.second
    grid[secondCell!!.first] = secondCell.second
    return grid
}

fun createEmptyGrid(): MutableMap<Cell, Int?> {
    for (y in 0 until 4) {
        for (x in 0 until 4) {
            val cell = Cell(x, y)
            grid[cell] = null
        }
    }
    return grid
}

fun spawnCell(): Pair<Cell, Int>? {
    val emptyCells = getEmptyCells()
    if (emptyCells.isEmpty()) return null
    val chosenCell = emptyCells[Random.nextInt(0, emptyCells.size - 1)]
    val value = if ((0..9).random() > 0) 2 else 4
    grid[chosenCell] = value
    return chosenCell to value
}

fun getEmptyCells(): List<Cell> {
    val emptyCells = mutableListOf<Cell>()
    for (y in 0 until 4) {
        for (x in 0 until 4) {
            val cell = Cell(x, y)
            if (grid[cell] != null) continue
            emptyCells.add(cell)
        }
    }
    return emptyCells
}

fun getCellColor(number: Int?): Color {
    when (number) {
        2 -> return Color(0xffe3c3a6)
        4 -> return Color(0xffece0c9)
        8 -> return Color(0xffffb278)
        16 -> return Color(0xfffe965c)
        32 -> return Color(0xfff77b61)
        64 -> return Color(0xffeb5837)
        128 -> return Color(0xffecdc92)
        256 -> return Color(0xfff0d479)
        512 -> return Color(0xfff4ce60)
        1024 -> return Color(0xfff8c847)
        2048 -> return Color(0xffffc22e)
        4096 -> return Color(0xff6882f9)
        8192 -> return Color(0xff3355f7)
    }
    return Color(0xffebe7e1)
}




