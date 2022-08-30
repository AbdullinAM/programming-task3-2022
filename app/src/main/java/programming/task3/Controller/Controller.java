package programming.task3.Controller;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javafx.util.Pair;
import programming.task3.Core.Board;
import programming.task3.Core.BoardListener;
import programming.task3.Core.Checkers;

public class Controller implements BoardListener {

    Board board = new Board();

    @FXML
    private GridPane gridPaneLeft;
    @FXML
    private GridPane gridPaneRight;



    public Controller() {
        board.setBoardListener(this);
    }

    public Pair<GridPane, GridPane> getGridPanePair() {
        return new  Pair<>(gridPaneLeft, gridPaneRight);
    }



    public void turnMade() {
        updateBoard();
//        updateTurns();
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
        for (int i = 0; i < quantity; i++) {
            Circle circle = new Circle(20);
            circle.setFill(board.getBoard()[x].getColour().getImagePattern());
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

    public void updateBoard(){
        for (int i = 0; i < 24 ; i++) {
            drawCheckers(i);
        }


    }

}

