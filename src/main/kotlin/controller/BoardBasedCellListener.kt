package controller

import core.Board
import core.Cell

class BoardBasedCellListener(private val board: Board) : CellListener {
    override fun cellClicked(cell: Cell) {
        if (board.gameOver() == null) {
            board.makeTurn(cell)
        }
    }
}