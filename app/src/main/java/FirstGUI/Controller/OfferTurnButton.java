package FirstGUI.Controller;

import FirstGUI.Model.Model;
import javafx.scene.control.Button;

public class OfferTurnButton extends Button{

//    private static OfferTurnButton =

    public OfferTurnButton(int column, Model model, Cntrllr cntrllr) {
        this.setOpacity(0.0);
        this.setPrefWidth(20);
        this.setPrefHeight(20);
        this.setOnMouseClicked(event -> {
            cntrllr.setSelectedColumn(column);
            cntrllr.updateBoard();
            model.getPossibleTurns(column)
                    .forEach(possibleTurn ->
                    cntrllr.quartersOfField()[possibleTurn / 6]
                            .add(new ConfirmTurnButton(column, possibleTurn, model, cntrllr), possibleTurn % 6, 14 - model.howManyChipsInColumn(possibleTurn)));
        });
    }

}
