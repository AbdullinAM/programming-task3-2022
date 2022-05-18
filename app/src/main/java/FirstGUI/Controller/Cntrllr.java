package FirstGUI.Controller;

import FirstGUI.Model.ChipColor;
import FirstGUI.Model.Model;
import FirstGUI.Model.ModelListener;
import FirstGUI.Model.GroupOfChips;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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


public class Cntrllr implements ModelListener {

    public Cntrllr() {
        model.listener = this;
        chipsImages.put(ChipColor.WHITE, new ImagePattern(new Image("white3.png")));
        chipsImages.put(ChipColor.BLACK, new ImagePattern(new Image("black1.png")));
        model.randomFirstTurn();
    }

//  Поля и методы для основного окна
    private Model model = new Model();

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
    }//Почему-то если подобный массив занести в поле, то quarter1,quarter2... указывают на null.
    // Поэтому сделана функция возвращающая новый массив.

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
        GroupOfChips[][] field = model.getField().getCurrent();
        Color green = new Color(0.0,1.0,0.0,1.0);
        Color yellow = new Color(1.0, 1.0, 0.0, 1.0);
        boolean atLeastOneOfferTurnButtonAdded = false;
        /* for quarter of field */
        for (int i = 0; i < 4; i++) {
            quartersOfField()[i].getChildren().clear();
            /* for column in quarter*/
            for (int j = 0; j < 6; j++) {
                GroupOfChips chipsInColumn = field[i][j];
                List<Integer> probableTurns = model.getPossibleTurns(i*6+j);
                int numberOfChips = chipsInColumn.getQuantity();
                if (numberOfChips > 0) {
                    ChipColor colorOfChips = chipsInColumn.getColor();
                    /* for chip in column */
                    for (int k = 0; k < numberOfChips; k++) {
                        /*Рисуем круги фишек*/
                        quartersOfField()[i].add(new Circle(15, chipsImages.get(colorOfChips)), j, 14 - k);
                    }
                    /*Рисуем зелёные круги для кнопок предложения хода если ещё не выбрана никакая колонка фишек.
                     *По нажатию упомянутой кнопки индекс её колонки будет запомнен в поле selectedColumn. */
                    if (selectedColumn == -1)
                        if (colorOfChips == model.getCurrentTurn() && !probableTurns.isEmpty()) {
                            Button lastChipButton = new OfferTurnButton(i * 6 + j, model, this);
                            quartersOfField()[i].add(new Circle(10, green), j, 15 - numberOfChips);
                            quartersOfField()[i].add(lastChipButton, j, 15 - numberOfChips);
                            atLeastOneOfferTurnButtonAdded = true;
                        }
                    /*Если какая-то колонка выбрана для хода, то выделяем её жёлтым цветом*/
                    if (selectedColumn == i * 6 + j) {
                        quartersOfField()[i].add(new Circle(10, yellow), j, 15 - numberOfChips);
                    }
                }
            }
        }
        /*Если какая-то колонка выбрана для хода, то рисуем зелёные круги в местах куда можно cходить.*/
        if (selectedColumn > -1) {
            List<Integer> probableTurns = model.getPossibleTurns(selectedColumn);
            probableTurns.forEach(position ->
                    quartersOfField()[position/6].add(new Circle(10, green),position%6, 14-field[position/6][position%6].getQuantity()));
        } else {
            /*Если игроку некуда походить (не добавлено ни одной offerTurnButton), а ходы (turnsLeft) у него остались,
             * то ход передаём оппоненту.*/
            if (!atLeastOneOfferTurnButtonAdded && !model.getTurnsLeft().isEmpty()) {
                model.passTheTurn();
                passTurnAlert();
            }
        }
        /*Winning conditions*/
        if (model.getField().winnerIs() != null) winnerAlert(model.getField().winnerIs());
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


    /*Я пытался сделать картинки полями перечисления ChipColors, но тогда невозможно тестировать,
     так как ImagePattern не хочет инциализироваться вне javafx thread*/
    private Map<ChipColor, ImagePattern> chipsImages = new HashMap<>();

    //    Вспомогательные методы
    public String getColorNotationOfCurrentTurn(){
        if (model.getCurrentTurn() == ChipColor.BLACK) return "черные"; else return "белые";
    }

    public void passTurnAlert (){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Вы не можете походить ни одной фишкой, ход передаётся оппоненту ");
        alert.setHeaderText(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alertStage.show();
    }

    private void winnerAlert(ChipColor winner){
        String s = winner == ChipColor.BLACK? "чёрные" : "белые";
        Alert alert = new Alert(Alert.AlertType.WARNING,"Победили " + s);
        alert.setHeaderText(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alertStage.show();
    }
}
