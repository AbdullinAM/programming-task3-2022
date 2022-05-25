package com.example.coursework.core

import java.lang.IllegalStateException


enum class Directions {
    UP, DOWN, LEFT, RIGHT;

    fun getVector(): Vector {
        return when (this) {
            UP -> Vector(0, -1)
            DOWN -> Vector(0, 1)
            LEFT -> Vector(-1, 0)
            RIGHT -> Vector(1, 0)
            else -> throw IllegalStateException()
        }
    }
}