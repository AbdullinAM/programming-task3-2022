package com.example.coursework.core

// Расположение клетки
data class Cell(val row: Int, val column: Int) {


    fun moveForward(vector: Vector): Cell {
        return Cell(vector.x + this.row, vector.y + this.column)
    }

    fun moveBackwards(vector: Vector): Cell {
        return Cell(this.row - vector.x, this.column - vector.y)
    }
}