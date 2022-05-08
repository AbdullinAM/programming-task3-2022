package tornadofx

import javafx.application.Application
import javafx.scene.control.ButtonBar
import javafx.stage.Stage


class CheckersApp: App(CheckersView::class) {
    override fun start(stage: Stage) {
        if (StartDialog().showAndWait().get().buttonData == ButtonBar.ButtonData.OK_DONE)
            super.start(stage)
    }
}

fun main(args: Array<String>) {
    Application.launch(CheckersApp::class.java, *args)
}