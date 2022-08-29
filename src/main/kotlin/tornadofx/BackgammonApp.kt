package tornadofx

import controller.BoardController
import javafx.application.Application
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.text.Text
import javafx.stage.Stage


class BackgammonApp: App() {

@FXML
lateinit var actionTarget: Text

    override fun start(stage: Stage) {
        val loader = FXMLLoader(this::class.java.classLoader.getResource("main.fxml"))
//        val root = FXMLLoader.load<Parent>(this::class.java.classLoader.getResource("main.fxml"))
        val root = loader.load<Parent>()
        val scene = Scene(root, 744.0, 754.0)
        val controller = loader.getController<BoardController>()
        println(root.getChildList())
        stage.title = "Backgammon game"
        stage.scene = scene
        stage.show()
        controller.updateBoard()
    }
}

fun main(args: Array<String>) {
    Application.launch(BackgammonApp::class.java, *args)
}
