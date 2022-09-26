package programming.task3.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;
import programming.task3.Core.Board;
import programming.task3.Core.BoardListener;
import programming.task3.Core.Checkers;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;


public class Controller implements BoardListener {

    @FXML
    public GridPane gridPaneLeft;
    @FXML
    public GridPane gridPaneRight;
    @FXML
    private AnchorPane leftMenu;
    @FXML
    private Label showDice;

    Board board = new Board();

    public Controller() {
        board.setBoardListener(this);
    }

    public void startGame(){
        board.throwDices();
    }

    public Pair<GridPane, GridPane> getGridPanePair() {
        return new  Pair<>(gridPaneLeft, gridPaneRight);
    }

    private Integer selectedCell = -1;

    public void setSelectedCell(int cell){
        if (selectedCell < -1 || selectedCell > 23){
            throw new IllegalArgumentException("tried to select cell out of bounds");
        }
        selectedCell = cell;
    }

    public void turnMade() {
        updateBoard();
    }

    public void dicesRolled(int d1, int d2){
        showDice.setText(d1 + " - " + d2);
    }

    public AnchorPane getAnchorPaneByIndex(int x){
        Pair<GridPane, GridPane> gridPanePair = getGridPanePair();

        if(x < 6){ return  (AnchorPane) gridPanePair.getValue().getChildren().get(x);}
        if(x < 12){ return (AnchorPane) gridPanePair.getKey().getChildren().get(x);}
        if(x < 18){ return (AnchorPane) gridPanePair.getKey().getChildren().get(x % 12);}
        if(x < 24){ return (AnchorPane) gridPanePair.getValue().getChildren().get(x % 12);}
        return null;
    }

    public void drawCheckers(int x){
        int quantity = board.getBoard()[x].getQuantity();
        ImagePattern white = new ImagePattern(new Image("/White.png"));
        ImagePattern black = new ImagePattern(new Image("/Black.png"));
        for (int i = 0; i < quantity; i++) {
            Circle circle = new Circle(20);
            if (board.getBoard()[x].getColour() == Checkers.WHITE){
                circle.setFill(white);
            } else {
                circle.setFill(black);
            }
            circle.setCenterX(24);
            if (x > 11 ){
                circle.setCenterY(296 - 17 * i);
            } else {
                circle.setCenterY(20 + 17 * i);
            }
            circle.setStroke(Color.GRAY);
            getAnchorPaneByIndex(x).getChildren().add(circle);
        }
    }

    private void skipTurn(){
        for (int i = 0; i < 24; i++){
            AnchorPane ap = getAnchorPaneByIndex(i);
            if (ap.lookup(".button") != null){
                return;
            }
        }
        if (leftMenu.lookup(".button") != null){
            return;
        }
        board.turnOpposite();
        updateBoard();
}

    public void updateBoard(){
        for (int i = 0; i < 24 ; i++) {
            getAnchorPaneByIndex(i).getChildren().clear();
            drawCheckers(i);
            CellButtons cellButtons = new CellButtons();

            if (i > 11){
                cellButtons.setLayoutY(37);
            }
            if (board.getBoard()[i].getColour() == board.getTurn()
                    && !board.allValidMoves(i).isEmpty()) {
                getAnchorPaneByIndex(i).getChildren().add(cellButtons.PossibleTurns(i, board, this));
            }
        }
        leftMenu.getChildren().remove(leftMenu.lookup(".button"));
        if(board.openExit()){
            ExitButton leftMenuButton = new ExitButton();
            leftMenu.getChildren().add(leftMenuButton.exitButton(board, this));
            if (board.winner() != Checkers.NO_COLOR){
                endGame(board.winner());
            }

        }
        if (!board.openExit()) {
            skipTurn();
        }

    }


    private void endGame(Checkers winner) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Press ok to restart, cancel to close the game");
        String win;
        if (winner == Checkers.BLACK){
            win = "Black";
        } else if (winner == Checkers.WHITE){
            win = "White";
        } else {
            return;
        }
        alert.setTitle("winner is - "+ win);
        Optional<ButtonType> restart = alert.showAndWait();
        if (restart.get() == ButtonType.OK){
            board.restartOfTheGame();
            updateBoard();
        } else if (restart.get() == ButtonType.CANCEL){
            leftMenu.getScene().getWindow().hide();
        }
    }
}


