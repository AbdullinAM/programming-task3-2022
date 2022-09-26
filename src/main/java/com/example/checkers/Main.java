package com.example.checkers;

import com.example.checkers.controller.BoardController;
import com.example.checkers.view.GameBoardGrid;
import com.example.checkers.view.Render;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setHeight(1035);
        stage.setWidth(1010);
        stage.setResizable(false);
        stage.setTitle("Checkers");

        GameBoardGrid grid = new GameBoardGrid(1000);
        BoardController controller = new BoardController();
        grid.setOnFieldClickListener(controller.getOnFieldClick());
        controller.setRender(grid);

        grid.fillGrid();


        stage.setScene(new Scene(grid, 1000, 1000));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}