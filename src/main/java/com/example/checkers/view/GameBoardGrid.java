package com.example.checkers.view;

import com.example.checkers.controller.entity.Field;
import com.example.checkers.controller.entity.GridState;
import com.example.checkers.view.entity.FieldButton;
import com.example.checkers.listener.OnFieldClick;
import com.example.checkers.view.entity.FieldState;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Pair;
import com.example.checkers.controller.entity.*;

import java.net.URL;

import static com.example.checkers.controller.entity.GridState.BOARD_CONSTANT;

public class GameBoardGrid extends GridPane implements Render {

    private final int fieldSize;
    private OnFieldClick onFieldClickListener;

    private FieldButton[][] buttons;

    public GameBoardGrid(int height) {
        fieldSize = height / BOARD_CONSTANT;
        createBoard();
        setGridLinesVisible(true);
    }

    private void createBoard() { //ДОБАВИТЬ ШАПКУ ИГРЫ С ФУНКЦИЕЙ РЕСТАРТА*
        for (int i = 0; i < BOARD_CONSTANT; i++) {
            getColumnConstraints().add(new ColumnConstraints(fieldSize));
            getRowConstraints().add(new RowConstraints(fieldSize));
        }
    }

    public void setOnFieldClickListener(OnFieldClick onFieldClickListener) {
        this.onFieldClickListener = onFieldClickListener;
    }

    private ImageView whiteChecker() {
        URL path = getClass().getResource("/whitechecker.png");
        assert path != null;
        ImageView imageView = new ImageView(path.toString());
        imageView.setFitHeight(fieldSize - 10);
        imageView.setFitWidth(fieldSize - 10);
        imageView.setTranslateX(-2);
        return imageView;
    }

    private ImageView whiteRoyal() {
        URL path = getClass().getResource("/whiteroyal.png");
        assert path != null;
        ImageView imageView = new ImageView(path.toString());
        imageView.setFitHeight(fieldSize - 10);
        imageView.setFitWidth(fieldSize - 10);
        imageView.setTranslateX(-2);
        return imageView;
    }

    private ImageView blackChecker() {
        URL path = getClass().getResource("/blackchecker.png");
        assert path != null;
        ImageView imageView = new ImageView(path.toString());
        imageView.setFitHeight(fieldSize - 10);
        imageView.setFitWidth(fieldSize - 10);
        imageView.setTranslateX(-2);
        return imageView;
    }

    private ImageView blackRoyal() {
        URL path = getClass().getResource("/blackroyal.png");
        assert path != null;
        ImageView imageView = new ImageView(path.toString());
        imageView.setFitHeight(fieldSize - 10);
        imageView.setFitWidth(fieldSize - 10);
        imageView.setTranslateX(-2);
        return imageView;
    }

    private FieldButton createField(int x, int y) {
        FieldButton fieldButton = new FieldButton(new Pair<>(x, y));
        fieldButton.setPrefWidth(fieldSize);
        fieldButton.setPrefHeight(fieldSize);
        fieldButton.addEventHandler(
                MouseEvent.MOUSE_CLICKED,
                event -> {
                    if (onFieldClickListener != null) {
                        onFieldClickListener.onClick(
                                fieldButton.getPoint()
                        );
                    }
                }
        );
        return fieldButton;
    }

    @Override
    public void render(GridState state) {
        int counter = 0;
        String color;
        Field[][] fields = state.getFieldButtons();
        for (int i = 0; i < BOARD_CONSTANT; i++) {
            counter++;
            for (int j = 0; j < BOARD_CONSTANT; j++) {
                Field field = fields[i][j];
                FieldButton button = buttons[i][j];
                if (0 == counter % 2) color = "white";
                else color = "dimgrey";
                if (field.getChecker() != null && field.getChecker().isWhiteColour()) {
                    if (field.getChecker().isRoyal()) buttons[i][j].setGraphic(whiteRoyal());
                    else buttons[i][j].setGraphic(whiteChecker());
                }
                if (field.getChecker() != null && !field.getChecker().isWhiteColour()) {
                    if (field.getChecker().isRoyal()) buttons[i][j].setGraphic(blackRoyal());
                    else buttons[i][j].setGraphic(blackChecker());
                }
                if (field.getChecker() == null) buttons[i][j].setGraphic(null);
                counter++;
                button.setStyle(
                        createFieldStyle(
                                field.getState(), color
                        )
                );
            }
        }
    }

    private String createFieldStyle(FieldState state, String color) {
        switch (state) {
            case ATTACK:
                color = "red";
                break;
            case POSSIBLE:
                color = "green";
                break;
        }
        return "-fx-background-color: " + color;
    }

    public void fillGrid() {
        buttons = new FieldButton[BOARD_CONSTANT][BOARD_CONSTANT];
        for (int j = 0; j < BOARD_CONSTANT; j++) {
            for (int i = 0; i < BOARD_CONSTANT; i++) {
                FieldButton fieldButton = createField(i, j);
                buttons[i][j] = fieldButton;
                add(fieldButton, i, j);
            }
        }
    }
}

