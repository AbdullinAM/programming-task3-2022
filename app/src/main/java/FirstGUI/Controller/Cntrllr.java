package FirstGUI.Controller;

import FirstGUI.Model.ChipColor;
import FirstGUI.Model.Model;
import FirstGUI.Model.ModelListener;
import FirstGUI.Model.GroupOfChips;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.List;


public class Cntrllr implements ModelListener {

    public Cntrllr() {
        model.listener = this;
    }

//  ���� � ������ ��� ��������� ����
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
    }//������-�� ���� �������� ������ ������� � ����, �� quarter1,quarter2... ��������� �� null.
    // ������� ������� ������� ������������ ����� ������.

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

    public void columnChoosed(int column) {
        selectedColumn = column;
    }

//    private List<Integer> probableTurns = new ArrayList<>();
//
//    public void setProbableTurns(List<Integer> probableTurns) {
//
//    }

    public void updateBoard(){
        GroupOfChips[][] field = model.getField().getCurrent();
        Color white = new Color(1.0,1.0,1.0,1.0);
        Color green = new Color(0.0,1.0,0.0,1.0);
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
                    Color colorOfChips = chipsInColumn.getNormalizedColor();
                    /* for chip in column */
                    for (int k = 0; k < numberOfChips; k++) {
                        /*������ ����� �����*/
                        quartersOfField()[i].add(new Circle(10, colorOfChips), j, 14 - k);
                        /*������ ������ ����� ��� ������ ����������� ���� ���� ��� �� ������� ������� ������� �����
                        *�� ������� ���������� ������ ������ � ������� ����� �������� � ���� selectedColumn. */
                        if (selectedColumn == -1)
                            if (colorOfChips == model.getCurrentTurn().normalizedColor && !probableTurns.isEmpty()) {
                                Button lastChipButton = new OfferTurnButton(i * 6 + j, model, this);
                                quartersOfField()[i].add(new Circle(5, green), j, 15 - numberOfChips);
                                quartersOfField()[i].add(lastChipButton, j, 15 - numberOfChips);
                                atLeastOneOfferTurnButtonAdded = true;
                        }
                        /*���� �����-�� ������� ������� ��� ����, �� �������� � ����� ������*/
                        if (selectedColumn == i * 6 + j) {
                            quartersOfField()[i].add(new Circle(10, new Color(1.0, 1.0, 0.0, 1.0)), j, 15 - numberOfChips);
                        }
                    }
                }
            }
        }
        /*������ ������ ������������� ���� ������� �������*/
        if (selectedColumn > -1) {
            List<Integer> probableTurns = model.getPossibleTurns(selectedColumn);
            probableTurns.forEach(position ->
                    quartersOfField()[position/6].add(new Circle(10, green),position%6, 14-field[position/6][position%6].getQuantity()));
        } else {
            /*���� ������ ������ �������� (�� ��������� �� ����� offerTurnButton), � ���� (turnsLeft) � ���� ��������,
             * �� ��� ������� ���������.*/
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

//  ��������������� ������
    public String getColorNotationOfCurrentTurn(){
        if (model.getCurrentTurn() == ChipColor.BLACK) return "������"; else return "�����";
    }

    public void passTurnAlert (){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"�� �� ������ �������� �� ����� ������, ��� ��������� ��������� ");
        alert.setHeaderText(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alertStage.show();
    }

}
