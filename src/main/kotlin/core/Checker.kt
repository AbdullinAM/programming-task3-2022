package core

open class Checker(val color: Color) {

    fun isOpposite(other: Checker?) = other != null && other.color != this.color

}