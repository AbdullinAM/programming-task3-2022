package com.example.abc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Random;

public class Controller {

    @FXML
    private Canvas canvas;

    @FXML
    private Button figure_1;

    @FXML
    private Button figure_2;

    @FXML
    private MenuItem fin;

    @FXML
    private MenuItem finish;

    @FXML
    private Rectangle rec_1;

    @FXML
    private Rectangle rec_2;

    @FXML
    private MenuItem restart;


    @FXML
    void creator_1(ActionEvent event) {
        if (model.player1Finished) return;
        if (model.lastPlayer == 1) return;
        if (model.gameOver) return;
        Random R = new Random();
        int W = R.nextInt(Logic.N/2) + 1;
        int H = R.nextInt(Logic.N/2) + 1;
        rec_1.setWidth(W*15);
        rec_1.setHeight(H*15);
        var coord = model.canFit(H,W,1);
        if (coord == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Сообщение от игры");
            alert.setContentText(String.format("Блок размера %dx%d нет места.", H, W));
            alert.showAndWait();
            model.player1Finished = true;
        }
        drawCanvas();

    }

    void drawCanvas (){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,300,300);
        for (int row = 0;row <10; row ++){
            for (int col = 0;col <10; col ++){
                int p = model.getWhoCell(row,col);
                if (p <= 0) continue;
                else if (p == 1) gc.setFill(Color.RED);
                else if (p == 2) gc.setFill(Color.BLUE);
                gc.fillRect(col*30,row*30, 30, 30);
            }

        }
        for (int row = 0;row <10; row ++){
            for (int col = 0;col <10; col ++) {
                gc.strokeLine((col+1)*30,0,(col+1)*30,300);
                gc.strokeLine(0,row*30,300,row*30);
            }

        }
    }

    @FXML
    void creator_2(ActionEvent event) {
        if (model.player2Finished) return;
        if (model.lastPlayer == 2) return;
        if (model.gameOver) return;
        Random R = new Random();
        int W = R.nextInt(Logic.N/2) + 1;
        int H = R.nextInt(Logic.N/2) + 1;
        rec_2.setWidth(W*15);
        rec_2.setHeight(H*15);
        var coord = model.canFit(H,W,2);
        if (coord == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Сообщение от игры");
            alert.setContentText(String.format("Блок размера %dx%d нет места.", H, W));
            alert.showAndWait();
            model.player2Finished = true;
        } if (model.player1Finished || model.player2Finished) {
            int x1 = 0;
            int x2 = 0;
            for(int row = 0;row <10;row ++){
                for(int col = 0;col <10;col ++){
                    if (model.getWhoCell(row,col) == 1) {
                        x1 ++;
                    }
                    else if (model.getWhoCell(row,col) == 2){
                        x2 ++;
                    }
                }
            }
            drawCanvas();
            model.gameOver = true;
                String msg = "Победил ";
                if (x1>x2) msg = msg + "Красный ";
                else if (x1<x2) msg = msg + "Синий ";
                else msg = msg + "Ничья ";
                msg += String.format("Счет %d:%d", x1,x2);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("%s", msg));
                alert.showAndWait();
                model.player2Finished = true;
            }
        else {
            drawCanvas();
        }
    }

    @FXML
    void finish(ActionEvent event) throws IOException {
        MyApplication me = new MyApplication();
        me.changeScene("/finish.fxml");
    }

    @FXML
    void me(ActionEvent event) throws IOException {
        MyApplication m = new MyApplication();
        m.changeScene("/aboutMe.fxml");
    }

    @FXML
    void restart(ActionEvent event) throws IOException {
        MyApplication m = new MyApplication();
        m.changeScene("/MyApplication.fxml");
    }
    Logic model = new Logic();
}
