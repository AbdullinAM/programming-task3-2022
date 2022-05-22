package FirstGUI.Controller;

import FirstGUI.Model.ChipColor;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class CntrllrOfStartDialog implements Scoreboard{

    private String whitePlayerName = "Игрок 1";

    private String blackPlayerName = "Игрок 2";

    private int whiteScore = 0;

    private int blackScore = 0;

    @FXML
    private TextField playerInput1;

    @FXML
    private TextField playerInput2;

    @FXML
    private void startButtonClicked(){
        playerInput1.getScene().getWindow().hide();
        setPLayersNamesIfGivenNotEmpty( playerInput1.getText(), playerInput2.getText());
    }

    public void setPLayersNamesIfGivenNotEmpty(String nameOfWhite, String nameOfBlack){
        if (!nameOfWhite.trim().equals("")) whitePlayerName = nameOfWhite;
        if (!nameOfBlack.trim().equals("")) blackPlayerName = nameOfBlack;
    }

    @Override
    public String getName(ChipColor winner) {
        if (winner == ChipColor.WHITE)
            return whitePlayerName;
        else
            return blackPlayerName;
    }

    @Override
    public void winnerIs(ChipColor winner) {
        if(winner == ChipColor.WHITE)
            whiteScore += 1;
        else
            blackScore += 1;
    }

    @Override
    public void changeColors() {
        String name = whitePlayerName;
        int score = whiteScore;
        whitePlayerName = blackPlayerName;
        blackPlayerName = name;
        whiteScore = blackScore;
        blackScore = score;
    }

    @Override
    public String getScore() {
        return whitePlayerName+" - "+whiteScore+":"+blackScore+" - "+blackPlayerName;
    }
}
