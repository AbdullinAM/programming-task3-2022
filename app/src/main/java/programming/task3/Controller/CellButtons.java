package programming.task3.Controller;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import programming.task3.Core.Board;

public class CellButtons extends Button {


    public CellButtons() {
        this.setOpacity(0.5);
        this.setPrefWidth(48);
        this.setPrefHeight(278);

    }

    public void clearButtons(int cell,Controller controller){

        for (int i = 0; i < 24 ; i++) {
            if (i != cell) {

                AnchorPane ap= controller.getAnchorPaneByIndex(i);
                ap.getChildren().remove(ap.lookup(".button"));
                //изпользуем lookup для поиска кнопки в AnchorPane
            }
        }
    }

    public Button PossibleTurns(int cell, Board board, Controller controller){

        this.setOnMouseClicked(action ->{
            if (action.getButton().equals(MouseButton.PRIMARY)) {
                controller.setSelectedCell(cell);
                controller.updateBoard();
                clearButtons(cell, controller);
                for (int validMove : board.allValidMoves(cell)) {
                    Button cellButtons = new CellButtons().moveButton(cell, validMove, board, controller);
                    if (validMove > 11) {
                        cellButtons.setLayoutY(37);
                    }
                    cellButtons.setStyle("-fx-background-color: #00ff00");
                    controller.getAnchorPaneByIndex(validMove).getChildren().add(cellButtons);
                }
            } else if (action.getButton().equals(MouseButton.SECONDARY)){
                controller.updateBoard();
            }

        });
        return this;
}


    public Button moveButton(int initialPos, int finalPos, Board board, Controller controller){

        this.setOnMouseClicked(action -> {
            controller.setSelectedCell(-1);
            board.move(initialPos, finalPos);
            controller.updateBoard();
        });


        return this;
    }




}
