package programming.task3.Controller;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import programming.task3.Core.Board;
import programming.task3.Core.Checkers;


public class CellButtons extends Button {

    public CellButtons() {
        this.setOpacity(0.5);
        this.setPrefWidth(48);
        this.setPrefHeight(278);
    }

    public void clearButtons(int cell,Controller controller){
        for (int i = 0; i < 24 ; i++) {
            if (i != cell) {
                AnchorPane ap = controller.getAnchorPaneByIndex(i);
                ap.getChildren().remove(ap.lookup(".button"));
            }
        }
    }

    public Button PossibleTurns(int cell, Board board, Controller controller){
        this.setOnMouseClicked(action ->{
            if (action.getButton().equals(MouseButton.PRIMARY)){
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

    public Button throwButton(int cell, Board board, Controller controller){
        if (cell > 11) {
            this.setLayoutY(37);
        }
        this.setStyle("-fx-background-color: #4169e1");
        this.setOnMouseClicked(action ->{
            controller.setSelectedCell(-1);
            board.throwChecker(cell);
            controller.updateBoard();
        });
        return this;
    }
}

class ExitButton extends Button {

    public ExitButton() {
        this.setOpacity(1);
        this.setPrefWidth(100);
        this.setPrefHeight(100);
        this.setStyle("-fx-background-color: #4169e1");
        this.setLayoutX(25);
        this.setLayoutY(335);
        this.setText("Exit");
    }

    public void clearNotExitButtons(Controller controller){
        for (int i = 0; i < 24 ; i++) {
            AnchorPane ap = controller.getAnchorPaneByIndex(i);
            ap.getChildren().remove(ap.lookup(".button"));
        }
    }

    public Button exitButton(Board board, Controller controller){
        this.setOnMouseClicked(action ->{
            controller.updateBoard();
            if (action.getButton().equals(MouseButton.PRIMARY)){
                controller.updateBoard();
                clearNotExitButtons(controller);
                if (board.turn == Checkers.WHITE){
                    for (int i = 18; i < 24; i++){
                        if (board.canExitFromX(i)){
                            Button throwButton = new CellButtons().throwButton(i, board, controller);
                            controller.getAnchorPaneByIndex(i).getChildren().add(throwButton);
                        }
                    }
                }
                else if (board.turn == Checkers.BLACK){
                    for (int i = 6; i < 12; i++){
                        if (board.canExitFromX(i)){
                            Button throwButton = new CellButtons().throwButton(i, board, controller);
                            controller.getAnchorPaneByIndex(i).getChildren().add(throwButton);
                        }
                    }
                }
            } else if (action.getButton().equals(MouseButton.SECONDARY)){
                controller.updateBoard();
            }
        });
        return this;
    }
}
