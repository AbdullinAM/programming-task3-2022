package core

open class Checker(private val color: Color) {

    fun isOpposite(other: Checker?) = other != null && other.color != this.color

}