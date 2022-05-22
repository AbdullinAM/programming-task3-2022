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
    private TextField whitePlayer;

    @FXML
    private TextField blackPlayer;

    @FXML
    private void startButtonClicked(){
        whitePlayer.getScene().getWindow().hide();
        setPLayersNamesIfGivenNotEmpty( whitePlayer.getText(), blackPlayer.getText());
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
}
