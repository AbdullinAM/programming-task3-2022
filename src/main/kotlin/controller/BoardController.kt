package controller

import core.Board
import javafx.fxml.FXML

import javafx.scene.layout.GridPane
//import core.Color

import javafx.scene.paint.Color

import javafx.scene.shape.Circle



class BoardController {

    @FXML
    var gridOne: GridPane = GridPane()
    @FXML
    var gridTwo: GridPane = GridPane()
    @FXML
    var gridThree: GridPane = GridPane()
    @FXML
    var gridFour: GridPane = GridPane()

    private fun getListOfGrids(): List<GridPane> {
        return listOf(gridOne, gridTwo, gridThree, gridFour)
    }

    private var board = Board()

//    fun getImageOfChecker(color: Color): ImagePattern {
//        if (color == Color.BLACK) return ImagePattern(Image("BlackChecker.png"))
//        if (color == Color.WHITE) return ImagePattern(Image("WhiteChecker.png"))
//    }

    private fun convertIndicesForGridPane(number: Int): Pair<Int, Int> {
        return Pair(number / 6, number % 6)
    }


    private fun getColor(i: Int): Color {
        val listOfPos = board.listOfPositions
        var currentColor = Color(0.0,0.0,0.0,0.0)
        when (listOfPos[i].color) {
            core.Color.BLACK -> currentColor = Color(0.0, 0.0, 0.0, 1.0)
            core.Color.WHITE -> currentColor = Color(1.0, 1.0, 1.0, 1.0)
        }
        return currentColor
    }

    fun updateBoard() {
        val listOfPos = board.listOfPositions
        for (i in listOfPos.indices) {
            if (listOfPos[i].isNotEmpty()) {
                val convertedIndex = convertIndicesForGridPane(i)
                for (j in 0 until listOfPos[i].count) {
                    getListOfGrids()[convertedIndex.first].add(
                        Circle(13.0, getColor(i)),
                        convertedIndex.second + 5, j)
                }
            }
        }

//        for (i in 1..15) {
//            gridOne.add(Circle(13.0, Color(1.0,1.0,1.0,1.0)), 5, i)
//        }
//        for (i in 1..15) {
//            gridThree.add(Circle(13.0, Color(0.0,0.0,0.0,1.0)), 5, i)
//        }
    }

    private var selectedColumn = -1

    fun setSelectedColumn(column: Int) {
        selectedColumn = column
    }
}