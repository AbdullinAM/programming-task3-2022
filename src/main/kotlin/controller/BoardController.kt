package controller

import core.Board
import core.BoardListenerInterface
import core.Color
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import tornadofx.action
import tornadofx.add


class BoardController: BoardListenerInterface {

    @FXML
    var gridOne: GridPane = GridPane()
    @FXML
    var gridTwo: GridPane = GridPane()
    @FXML
    var gridThree: GridPane = GridPane()
    @FXML
    var gridFour: GridPane = GridPane()
    @FXML
    var anchorPane: AnchorPane = AnchorPane()

    private var selectedColumn = -1

    private fun getListOfGrids(): List<GridPane> {
        return listOf(gridOne, gridTwo, gridThree, gridFour)
    }

    private var board = Board(this)


    private fun convertIndicesForGridPane(number: Int): Pair<Int, Int> {
        //first is number of GridPane, second is position in GridPane
        return Pair(number / 6, number % 6)
    }


    private fun getColor(i: Int): ImagePattern {
        val listOfPos = board.listOfPositions
        var currentColor = ImagePattern(Image("/noImage.png"))
        when (listOfPos[i].color) {
            Color.BLACK -> currentColor = ImagePattern(Image("/BlackChecker.png"))
            Color.WHITE -> currentColor = ImagePattern(Image("/WhiteChecker.png"))
        }
        return currentColor
    }

    private fun drawThrowingButton(from: Int) {
        val button = Button()
        button.action {
            board.throwOutFromTheBoard(from)
            board.updateTurns()
            updateBoard()
        }
        if (board.currentTurn == Color.BLACK) {
            button.layoutX = 12.0
            button.layoutY = 370.0
            anchorPane.add(button)
        }
        else if (board.currentTurn == Color.WHITE) {
            button.layoutX = 715.0
            button.layoutY = 370.0
            anchorPane.add(button)
        }
    }

    override fun showDices(firstDice: Int, secondDice: Int) {
        val firstRectangle = Rectangle(50.0,50.0, ImagePattern(Image("/die$firstDice.png")))
        firstRectangle.x = 348.5
        firstRectangle.y = 400.0
        val secondRectangle = Rectangle(50.0,50.0, ImagePattern(Image("/die$secondDice.png")))
        secondRectangle.x = 348.5
        secondRectangle.y = 300.0
        anchorPane.add(firstRectangle)
        anchorPane.add(secondRectangle)
    }

    private fun addMakeMoveButton(to: Int, from: Int) {
        val button = Button()
        button.action {
            board.makeMove(from, to)
            selectedColumn = -1
            updateBoard()
        }
        val convertedIndex = convertIndicesForGridPane(to)
        getListOfGrids()[convertedIndex.first].add(button, convertedIndex.second, 14)
    }


    private fun addPossibleMoveButton(from: Int) {
        val button = Button()
        button.style = "-fx-background-color: #008000"
        button.action {
            for (i in board.possibleMoves(from)) {
                addMakeMoveButton(i, from)
                selectedColumn = from
            }
        }
        val convertedIndex = convertIndicesForGridPane(from)
        getListOfGrids()[convertedIndex.first].add(button, convertedIndex.second, 14)
    }

    private fun addThrowingButton(from: Int) {
        val button = Button()
        button.style = "-fx-background-color: #ff0000"
        button.action {
            updateBoard()
            drawThrowingButton(from)
        }
        val convertedIndex = convertIndicesForGridPane(from)
        getListOfGrids()[convertedIndex.first].add(button, convertedIndex.second, 14)
    }

    private fun clear() {
        for (i in getListOfGrids()) {
            i.children.clear()
        }
        anchorPane.children.remove(anchorPane.lookup(".button"))
    }

    //защита от случаев, когда остались ходы, но нет возможности походить на эту позицию т.к закрыта другой шашкой
    private fun checkingMovePossibility() {
        for (i in getListOfGrids()) {
            for (j in i.children) {
                if (j is Button) {
                    return
                }
            }
        }
        for (i in 0..24) {
            if (board.possibleToThrow(i)) {
                return
            }
        }
        changeCurrentPlayer()
    }

    fun updateBoard() {
        if (board.gameOverCheck() != null) {
            winnerAlertAndRestart(board.gameOverCheck())
        }
        board.checkPossibilityOfThrowing()
        val listOfPos = board.listOfPositions
        clear()
        board.updateTurns()
        for (i in listOfPos.indices) {
            if (listOfPos[i].isNotEmpty()) {
                val convertedIndex = convertIndicesForGridPane(i)
                for (j in 0 until listOfPos[i].count) {
                    getListOfGrids()[convertedIndex.first].add(
                        Circle(13.0, getColor(i)),
                        convertedIndex.second, 14 - j)
                }
                if (board.currentTurn == listOfPos[i].color && board.possibleMoves(i).isNotEmpty()) {
                    addPossibleMoveButton(i)
                }
                if (board.possibleToThrow(i)) {
                    addThrowingButton(i)
                }
            }
        }
        checkingMovePossibility()
    }

    private fun changeCurrentPlayer() {
        board.turns.clear()
        board.updateTurns()
        updateBoard()
    }

    private fun winnerAlertAndRestart(color: Color?) {
        val winnerAlert = Alert(Alert.AlertType.CONFIRMATION, "Нажмите ОК для рестарта")
        val winner = when (color) {
            Color.WHITE -> "Белые"
            Color.BLACK -> "Чёрные"
            else -> ""
        }
        winnerAlert.headerText = "Игра закончена, победитель $winner!"
        val result = winnerAlert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val restartAlert = Alert(Alert.AlertType.INFORMATION, "Произведён рестарт игры")
            restartAlert.show()
            board.clearAllBoard()
        }
        else {
            winnerAlert.hide()
        }
    }
}