package com.example.coursework.core

// Квадрат, который появляется на доске. Каждому присвоен свой номер и ID.
class Tile constructor(val number: Int, val identity: Int) {

    companion object {
        private var id = 0
    }

    constructor(number: Int): this(number, id++)

}