package FirstGUI.Controller;

import FirstGUI.Controller.Buttons.OfferTurnButton;
import FirstGUI.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static FirstGUI.Model.ChipColor.*;
import static FirstGUI.Model.Field.fieldSize;
import static FirstGUI.Model.Field.exit;

/*Контроллер и отрисовщик */
public class Cntrllr implements ModelListener {

    public Cntrllr() {
        chipImagesMap.put(WHITE, new ImagePattern(new Image("white3.png")));
        chipImagesMap.put(BLACK, new ImagePattern(new Image("black1.png")));
    }

//  Поля и методы для основного окна
    private final Model model = new Model().setModelListener(this);

    public Scoreboard scrboard;

    /*Я пытался сделать картинки полями перечисления ChipColors, но тогда невозможно тестировать,
     насколько я понял ImagePattern не хочет инциализироваться вне javafx thread*/
    private final Map<ChipColor, ImagePattern> chipImagesMap = new HashMap<>();

    @FXML
    private GridPane quarter1;

    @FXML
    private GridPane quarter2;

    @FXML
    private GridPane quarter3;

    @FXML
    private GridPane quarter4;

    public GridPane[] getGridPanes() {
        return new GridPane[] {quarter1,quarter2,quarter3,quarter4};
    }

    @FXML
    private Label diceLabel1;

    @FXML
    private Label diceLabel2;

    @FXML
    private Rectangle whiteExitRectangle;

    @FXML
    private Rectangle blackExitRectangle;

    @FXML
    private void diceButtonClicked(){
        model.rollNewTurns();
        updateTurns();
        updateBoard();
    }

    @Override
    public void turnMade() {
        updateBoard();
        updateTurns();
    }

    private Integer selectedColumn = -1;

    public void setSelectedColumn(int column) {
        selectedColumn = column;
    }

    public void updateBoard(){
        Field field = model.getField();
        GridPane[] gridPanes = getGridPanes();
        Color green = new Color(0.0,1.0,0.0,1.0);
        Color yellow = new Color(1.0, 1.0, 0.0, 1.0);
        boolean atLeastOneOfferTurnButtonAdded = false;
        /* Clear the field */
        for (GridPane gridPane : gridPanes) {
            gridPane.getChildren().clear();
        }
        /* for column of field */
        for (int i = 0; i < fieldSize; i++) {
            GroupOfChips chipsInColumn = field.get(i);
            int numberOfChips = chipsInColumn.getQuantity();
            if (numberOfChips > 0) {
                ChipColor colorOfChips = chipsInColumn.getColor();
                /* for chip in column */
                for (int k = 0; k < numberOfChips; k++) {
                    /*Рисуем круги фишек*/
                    gridPanes[i/6].add(new Circle(13, chipImagesMap.get(colorOfChips)), i%6, 14 - k);
                }
                /*Рисуем зелёные круги для кнопок предложения хода если ещё не выбрана никакая колонка фишек.
                 *По нажатию упомянутой кнопки индекс её колонки будет запомнен в поле selectedColumn. */
                if (selectedColumn == -1 && colorOfChips == model.getCurrentTurn()) {
                    if (!model.getPossibleTurns(i).isEmpty()) {
                        Button lastChipButton = new OfferTurnButton(i, model, this);
                        gridPanes[i / 6].add(new Circle(9, green), i % 6, 15 - numberOfChips);
                        gridPanes[i / 6].add(lastChipButton, i % 6, 15 - numberOfChips);
                        atLeastOneOfferTurnButtonAdded = true;
                    }
                }
                /*Если какая-то колонка выбрана для хода, то выделяем её жёлтым цветом*/
                if (selectedColumn == i) {
                    gridPanes[i/6].add(new Circle(9, yellow), i%6, 15 - numberOfChips);
                }
            }

        }
        /*Если какая-то колонка выбрана для хода, то рисуем зелёные круги в местах куда можно cходить.*/
        if (selectedColumn > -1) {
            List<Integer> probableTurns = model.getPossibleTurns(selectedColumn);
            probableTurns.forEach(position -> {
                        if (position == exit){
                            openExit(field.get(selectedColumn).getColor());
                        }
                        else {
                            gridPanes[position / 6].add(new Circle(10, green), position % 6, 14 - field.get(position).getQuantity());
                        }
                    });
            gridPanes[selectedColumn/6].add(
                    new Buttons.RefuseTurnButton(this),selectedColumn%6,15-field.get(selectedColumn).getQuantity());
        } else {
            /*Если игроку нечем походить (не добавлено ни одной offerTurnButton), а ходы (turnsLeft) у него остались,
             * то ход передаём оппоненту, и выводим предупреждение.*/
            if (!atLeastOneOfferTurnButtonAdded && !model.isNoTurnsLeft()) {
                model.passTheTurn();
                passTurnAlert();
            }
        }
        if (field.winnerIs() != null) {
            winnerAlert(field.winnerIs());
        }

    }

    private void updateTurns(){
        List<Integer> turnsLeft =  model.getTurnsLeft();
        if (turnsLeft.size() >= 2) {
            diceLabel1.setText(turnsLeft.get(0).toString());
            diceLabel2.setText(turnsLeft.get(1).toString());
        }
        Integer currentNumberInLabel1 = Integer.parseInt(diceLabel1.getText());
        Integer currentNumberInLabel2 = Integer.parseInt(diceLabel2.getText());
        if (!turnsLeft.contains(currentNumberInLabel1)) {
            diceLabel1.setOpacity(0.1);
        } else {
            diceLabel1.setOpacity(1.0);
        }
        if (!turnsLeft.contains(currentNumberInLabel2)) {
            diceLabel2.setOpacity(0.1);
        } else {
            diceLabel2.setOpacity(1.0);
        }
    }

    void openExit(ChipColor color){
        Rectangle exitRect = color == WHITE? whiteExitRectangle : blackExitRectangle;
        exitRect.setOpacity(1);
        exitRect.setOnMouseClicked(event -> {
            List<Integer> turns = model.getTurnsLeft();
            model.makeTurn(selectedColumn, exit);
            closeExit(color);
        });
    }

    void closeExit(ChipColor color) {
        selectedColumn = -1;
        Rectangle exit = color == WHITE? whiteExitRectangle : blackExitRectangle;
        exit.setOpacity(0.0);
        exit.setOnMouseClicked(event -> {});
        updateBoard();
    }


    public void passTurnAlert (){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Вы не можете походить ни одной фишкой, ход передаётся оппоненту "
        );
        alert.setHeaderText(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alertStage.show();
    }

    private void winnerAlert(ChipColor winner){
        scrboard.winnerIs(winner);
        String winnerName = scrboard.getName(winner);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Счёт:\n"+scrboard.getScore()+"\nЕщё раунд?");
        alert.setHeaderText("Поздравляем, "  + winnerName + ", вы выиграли раунд!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            scrboard.changeColors();
            alert = new Alert(Alert.AlertType.INFORMATION,
                    "За белых теперь играет " + scrboard.getName(ChipColor.WHITE)
            );
            alert.setHeaderText(null);
            alert.show();
            model.restart();
        } else {
            alert.hide();
            diceLabel1.getScene().getWindow().hide();
        }
    }
}
