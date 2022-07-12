package controller

import core.Board

class BoardListener(private val board: Board) {
    fun positionClicked(from: Int, to: Int) {
        if (board.gameOverCheck() == null) {
            board.makeMove(from, to)
        }
    }
}