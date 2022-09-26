package com.example.abc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class finish {

    @FXML
    private Menu about;

    @FXML
    private MenuItem restart;

    @FXML
    private ImageView win_image;

    @FXML
    void me(ActionEvent event) throws IOException {
        MyApplication me = new MyApplication();
        me.changeScene("/aboutMe.fxml");
    }

    @FXML
    void restart (ActionEvent event) throws IOException {
        MyApplication me = new MyApplication();
        me.changeScene("/MyApplication.fxml");
    }

}

