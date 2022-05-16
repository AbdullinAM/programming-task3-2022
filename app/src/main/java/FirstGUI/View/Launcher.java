package FirstGUI.View;

import FirstGUI.Controller.Cntrllr;
import FirstGUI.Controller.CntrllrOfStartDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;


public class Launcher extends javafx.application.Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
//        Основная сцена
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
        Cntrllr controllerOfMainScene = loader.getController();
        controllerOfMainScene.updateBoard();

//        Стартовый диалог
//        loader = new FXMLLoader(getClass().getResource("/startDialog.fxml"));
//        Dialog preGameDialog = new Dialog();
//        preGameDialog.setDialogPane(loader.load());
//        preGameDialog.setTitle("Pregame settings");
//        preGameDialog.showAndWait();
//        CntrllrOfStartDialog controllerOfStartDialog = loader.getController();
//
//        //Алерт говорящий кто ходит первым
//        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Первыми ходят "+controllerOfMainScene.getColorNotationOfCurrentTurn());
//        alert.setHeaderText(null);
//        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
//        alertStage.setAlwaysOnTop(true);
//        alertStage.show();

    }

    public static void main(String[] args) {
        Application.launch();
    }
}
