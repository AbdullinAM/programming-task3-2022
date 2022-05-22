package FirstGUI.Controller;

import FirstGUI.Model.Model;
import javafx.scene.control.Button;

public class Buttons {

    public static class OfferTurnButton extends Button{

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

    public static class RefuseTurnButton extends Button {
        public RefuseTurnButton(Cntrllr cntrllr) {
            this.setOpacity(0.0);
            this.setPrefWidth(20);
            this.setPrefHeight(20);
            this.setOnMouseClicked(event -> {
                cntrllr.setSelectedColumn(-1);
                cntrllr.updateBoard();
            });
        }

    }

    public static class ConfirmTurnButton extends Button {
        public ConfirmTurnButton(int startColumn, int targetColumn, Model model, Cntrllr cntrllr) {
            this.setPrefHeight(20);
            this.setPrefWidth(20);
            this.setOpacity(0.0);
            this.setOnMouseClicked(event -> {
                        cntrllr.setSelectedColumn(-1);
                        cntrllr.updateBoard();
                        model.makeTurn(startColumn,targetColumn);
                    }
            );
        }
    }

}
