package com.example.abc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;

import java.io.IOException;

public class aboutMe {

    @FXML
    private Menu back;

    @FXML
    void comeback(ActionEvent event) throws IOException {
        MyApplication me = new MyApplication();
        me.changeScene("/MyApplication.fxml");
    }

}

