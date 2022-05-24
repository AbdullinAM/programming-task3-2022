package Dinosaur.Game;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class MainMenuController {
    //Контроллер — это Java класс, который отвечает в приложении за обработку
    // поведения и взаимодействия с пользователем.
    @FXML
    private Button button;

    @FXML
    private Pane d;

    public MainMenuController() throws FileNotFoundException {
    }

    public Scene2 scene2= new Scene2();

    @FXML
    void initialize() {

        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Stage stage = new Stage();
            try {
                scene2.start(stage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}