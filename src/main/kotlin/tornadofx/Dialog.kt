package tornadofx

import javafx.scene.control.*

class StartDialog: Dialog<ButtonType>() {
    init {
        title = "Russian Checkers"
        with(dialogPane) {
            headerText = "Start play ?"
            buttonTypes.add(ButtonType("Start Game", ButtonBar.ButtonData.OK_DONE))
            buttonTypes.add(ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE))
        }
    }
}