package FirstGUI.Controller;

import FirstGUI.Model.Model;
import javafx.scene.control.Button;

public class ConfirmTurnButton extends Button {
    public ConfirmTurnButton(int startColumn, int targetColumn, Model model, Cntrllr cntrllr) {
        this.setPrefHeight(20);
        this.setPrefWidth(20);
        this.setOpacity(0.0);
        this.setOnMouseClicked(event -> {
                cntrllr.columnChoosed(-1);
                cntrllr.updateBoard();
                model.makeTurn(startColumn,targetColumn);
                }
                );
    }
}
