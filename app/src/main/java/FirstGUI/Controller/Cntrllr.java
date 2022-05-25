package FirstGUI.Controller;

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
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*Контроллер и отрисовщик */
public class Cntrllr implements ModelListener {

    public Cntrllr() {
        model.listener = this;
        chipImagesMap.put(ChipColor.WHITE, new ImagePattern(new Image("white3.png")));
        chipImagesMap.put(ChipColor.BLACK, new ImagePattern(new Image("black1.png")));
    }

//  Поля и методы для основного окна
    private final Model model = new Model();

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

    public GridPane[] quartersOfField() {
        return new GridPane[] {quarter1,quarter2,quarter3,quarter4};
    }

    @FXML
    private Label diceLabel1;

    @FXML
    private Label diceLabel2;

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
        Color green = new Color(0.0,1.0,0.0,1.0);
        Color yellow = new Color(1.0, 1.0, 0.0, 1.0);
        boolean atLeastOneOfferTurnButtonAdded = false;
        /* Clear the field */
        for (int i = 0; i < 4; i++) {
            quartersOfField()[i].getChildren().clear();
        }
        /* for column of field */
        for (int i = 0; i < 24; i++) {
            GroupOfChips chipsInColumn = field.get(i);
            int numberOfChips = chipsInColumn.getQuantity();
            if (numberOfChips > 0) {
                List<Integer> probableTurns = model.getPossibleTurns(i);
                ChipColor colorOfChips = chipsInColumn.getColor();
                /* for chip in column */
                for (int k = 0; k < numberOfChips; k++) {
                    /*Рисуем круги фишек*/
                    quartersOfField()[i/6].add(new Circle(13, chipImagesMap.get(colorOfChips)), i%6, 14 - k);
                }
                /*Рисуем зелёные круги для кнопок предложения хода если ещё не выбрана никакая колонка фишек.
                 *По нажатию упомянутой кнопки индекс её колонки будет запомнен в поле selectedColumn. */
                if (selectedColumn == -1)
                    if (colorOfChips == model.getCurrentTurn() && !probableTurns.isEmpty()) {
                        Button lastChipButton = new Buttons.OfferTurnButton(i, model, this);
                        quartersOfField()[i/6].add(new Circle(9, green), i%6, 15 - numberOfChips);
                        quartersOfField()[i/6].add(lastChipButton, i%6, 15 - numberOfChips);
                        atLeastOneOfferTurnButtonAdded = true;
                    }
                /*Если какая-то колонка выбрана для хода, то выделяем её жёлтым цветом*/
                if (selectedColumn == i) {
                    quartersOfField()[i/6].add(new Circle(9, yellow), i%6, 15 - numberOfChips);
                }
            }

        }

        /*Winning conditions*/
        if (field.winnerIs() != null) winnerAlert(field.winnerIs());
        /*Если какая-то колонка выбрана для хода, то рисуем зелёные круги в местах куда можно cходить.*/
        if (selectedColumn > -1) {
            List<Integer> probableTurns = model.getPossibleTurns(selectedColumn);
            probableTurns.forEach(position ->
                    quartersOfField()[position/6].add(new Circle(10, green),position%6, 14-field.get(position).getQuantity()));
            quartersOfField()[selectedColumn/6].add(
                    new Buttons.RefuseTurnButton(this),selectedColumn%6,15-field.get(selectedColumn).getQuantity());
        } else {
            /*Если игроку нечем походить (не добавлено ни одной offerTurnButton), а ходы (turnsLeft) у него остались,
             * то ход передаём оппоненту, и выводим предупреждение.*/
            if (!atLeastOneOfferTurnButtonAdded && !model.getTurnsLeft().isEmpty()) {
                model.passTheTurn();
                passTurnAlert();
            }
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
        if (!turnsLeft.contains(currentNumberInLabel1)) diceLabel1.setOpacity(0.1); else diceLabel1.setOpacity(1.0);
        if (!turnsLeft.contains(currentNumberInLabel2)) diceLabel2.setOpacity(0.1); else diceLabel2.setOpacity(1.0);
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
