package FirstGUI.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CntrllrOfStartDialog {

    private String whitePlayerName = "Белые";

    private String blackPlayerName = "Чёрные";

    @FXML
    private TextField whitePlayer;

    @FXML
    private TextField blackPlayer;

    @FXML
    private void startButtonClicked(){
        whitePlayer.getScene().getWindow().hide();
        this.setPLayersNamesIfGivenNotEmpty( whitePlayer.getText(), blackPlayer.getText());
    }

    public void setPLayersNamesIfGivenNotEmpty(String nameOfWhite, String nameOfBlack){
        if (!nameOfWhite.trim().equals("")) whitePlayerName = nameOfWhite;
        if (!nameOfBlack.trim().equals("")) blackPlayerName = nameOfBlack;
    }

}
