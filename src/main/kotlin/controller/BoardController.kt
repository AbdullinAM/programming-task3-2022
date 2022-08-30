package controller

import core.Board
import javafx.fxml.FXML
import javafx.scene.image.Image

import javafx.scene.layout.GridPane

import javafx.scene.paint.ImagePattern

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
        //first is number of GridPane, second is position in GridPane
        return Pair(number / 6, number % 6)
    }


    private fun getColor(i: Int): ImagePattern {
        val listOfPos = board.listOfPositions
        var currentColor = ImagePattern(Image("/noImage.png"))
        when (listOfPos[i].color) {
            core.Color.BLACK -> currentColor = ImagePattern(Image("/BlackChecker.png"))
            core.Color.WHITE -> currentColor = ImagePattern(Image("/WhiteChecker.png"))
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
                        convertedIndex.second, 14 - j)
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