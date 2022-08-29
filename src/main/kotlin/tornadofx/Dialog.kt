package tornadofx

import javafx.scene.control.*

class StartDialog: Dialog<ButtonType>() {
    init {
        title = "Backgammon game"
        with(dialogPane) {
            headerText = "Click to start the game"
            buttonTypes.add(ButtonType("Start Game", ButtonBar.ButtonData.OK_DONE))
            buttonTypes.add(ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE))
        }
    }
}