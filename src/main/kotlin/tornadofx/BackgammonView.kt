package tornadofx

import controller.BoardListener
import core.Board
import core.BoardListenerInterface
import core.Color
import core.PositionOnBoard
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import java.io.File


//class BackgammonView: View(), BoardListenerInterface {
//
//    private val board = Board()
//    override val root = BorderPane()
//    private lateinit var statusLabel: Label
//    private var gameIsNotEnd = true
//    private val buttons = mutableMapOf<PositionOnBoard, Button>()
//
//
//    init {
//        title = "Backgammon"
//        val listener = BoardListener(board)
//        val board = Board()
//        //board.registerListener(this)
//            with(root) {
//                minHeight = 800.0
//                minWidth = 800.0
//            top {
//                vbox {
//                    menubar {
//                        menu("Game") {
//                            item("Restart").action {
//                                restartGame()
//                            }
//                            separator()
//                            item("Exit").action {
//                                this@BackgammonView.close()
//                            }
//                        }
//                    }
//                    toolbar {
//                        button(graphic = ImageView("/restart.png").apply {
//                            fitWidth = 16.0
//                            fitHeight = 16.0
//                        }).action {
//                            restartGame()
//                        }
//                    }
//                }
//            }
//            center {
//                style {
//                    backgroundImage += File("src/main/resources/background.png").toURI()
//                }
//                hbox {
//                    gridpane {
//                        hboxConstraints {
//                            marginRight = 10.0
//                            marginLeft = 80.0
//                            hGrow = Priority.ALWAYS
//                            useMaxHeight = true
//                        }
//                        row {
//                            gridpaneConstraints {
//                                marginBottom = 10.0
//                            }
//                            for (i in 11 downTo 6) {
//                                button(i.toString()) {
//                                    gridpaneColumnConstraints {
//                                        percentWidth = 7.0
//                                        useMaxHeight = true
//                                    }
//                                }
//                            }
//                            constraintsForRow(0).prefHeight = 100.0
//                        }
//
//                        row {
//                            gridpaneConstraints {
//                                marginTop = 10.0
//                            }
//                            for (i in 12..17) {
//                                button(i.toString()) {
//                                    gridpaneColumnConstraints {
//                                        percentWidth = 7.0
//                                        useMaxHeight = true
//                                    }
//                                }
//                            }
//                            constraintsForRow(1).prefHeight = 100.0
//                        }
//                    }
//
//                    gridpane()
//                }
//            }
//                    bottom {
//                        statusLabel = label("")
//                    }
//                }
//    }
//
//    fun getImage(position: Int) = when (board[position]?.color) {
//        Color.WHITE -> "./WhiteChecker.png"
//        else -> "./BlackChecker.png"
//    }
//
//    private fun restartGame() {
//        board.clear()
//        for (i in 0..23) {
//
//        }
//
//    }
//
//    fun currentStatus(from: Int, to: Int) {
//        val winner = board.gameOverCheck()
//
//        statusLabel.text = when {
//            winner == Color.BLACK -> {
//                gameIsNotEnd = false
//                "Congratulations, Black WON! Press 'Restart' to restart the game"
//            }
//            winner == Color.WHITE -> {
//                gameIsNotEnd = false
//                "Congratulations, White WON! Press 'Restart' to restart the game"
//            }
//            board.currentTurn == Color.WHITE ->
//                "White's turn"
//            else ->
//                "Black's turn"
//        }
//    //    if (from != null) {
//    //        for (i in 0..23) {
//    //            buttons[i]?.apply {
//    //                graphic = ImageView(getImage(i))
//    //                style {
//    //                    minHeight = 65.px
//    //                    minWidth = 65.px
//    //                    padding = box(0.px) }
//    //            }
//    //        }
//    //    }
//    }
//
//    override fun turnMade(position: MutableList<PositionOnBoard>) {
//        currentStatus(20, 10)
//    }
//}