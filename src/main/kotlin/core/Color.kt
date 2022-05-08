package core

enum class Color {
    WHITE, BLACK;

    fun opposite() = if (this == WHITE) BLACK else WHITE
}