package tornadofx

import controller.BoardController
import core.Board
import javafx.scene.control.Button
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

class Buttons {
    class OfferTurnButton(column: Int, core: Board, boardController: BoardController): Button() {
        init {
            opacity = 0.0
            prefWidth = 20.0
            prefHeight = 20.0
            this.onMouseClicked = EventHandler { event: MouseEvent? ->
                boardController.setSelectedColumn(column)
                core.possibleMoves(column)

            }
        }
    }
}