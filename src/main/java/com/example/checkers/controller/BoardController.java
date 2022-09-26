package com.example.checkers.controller;

import com.example.checkers.controller.entity.Field;
import com.example.checkers.controller.entity.GridState;
import com.example.checkers.view.Render;
import com.example.checkers.listener.OnFieldClick;
import com.example.checkers.view.entity.FieldState;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.EventListener;

import static com.example.checkers.controller.entity.GridState.BOARD_CONSTANT;

public class BoardController {

    private OnFieldClick onFieldClick;
    private Render render;
    private GridState state;
    private int lastX;
    private int lastY;
    ArrayList<Field> changeFields = new ArrayList<>();
    ArrayList<Field> attackFields = new ArrayList<>();
    boolean whiteTurn = true;

    public BoardController() {
        state = new GridState();
    }

    public OnFieldClick getOnFieldClick() {
        if (onFieldClick == null) onFieldClick = new OnFieldClick() {
            @Override
            public void onClick(Pair<Integer, Integer> cords) {
                int x = cords.getKey();
                int y = cords.getValue();

                int dirY;
                int direction;
                int right;
                int left;

                Field field = state.getFieldButtons()[x][y];//ДОПИСАТЬ ВЗЯТИЕ ШАШЕК(ПРОВЕРКУ ATTACK И СЛЕД КЛЕТКИ И ТД)
                Field activeField = state.getActiveField();

                attackFields.clear();
                attackFields = state.attackFields(whiteTurn);

                if (activeField != null && field.getState() != FieldState.POSSIBLE) {
                    activeField.setState(FieldState.EMPTY);
                    state.setActiveField(null);
                    for (Field fields : changeFields) {
                        fields.setState(FieldState.EMPTY);
                    }
                    changeFields.clear();
                }

                if (attackFields.isEmpty()) {

                    changeFields.add(field);

                    if (field.getChecker() != null && field.getChecker().isWhiteColour() == whiteTurn) {
                        if (field.getChecker().isRoyal()) {
                            dirY = y + 1;
                            for (right = x + 1; right < BOARD_CONSTANT && (dirY < BOARD_CONSTANT && dirY >= 0); right++, dirY++) {
                                if (state.getFieldButtons()[right][dirY].getChecker() != null) break;
                                else changeFields.add(state.getFieldButtons()[right][dirY]);
                            }
                            dirY = y + 1;
                            for (left = x - 1; left >= 0 && (dirY < BOARD_CONSTANT && dirY >= 0); left--, dirY++) {
                                if (state.getFieldButtons()[left][dirY].getChecker() != null) break;
                                else changeFields.add(state.getFieldButtons()[left][dirY]);
                            }
                            dirY = y - 1;
                            for (right = x + 1; right < BOARD_CONSTANT && (dirY < BOARD_CONSTANT && dirY >= 0); right++, dirY--) {
                                if (state.getFieldButtons()[right][dirY].getChecker() != null) break;
                                else changeFields.add(state.getFieldButtons()[right][dirY]);
                            }
                            dirY = y - 1;
                            for (left = x - 1; left >= 0 && (dirY < BOARD_CONSTANT && dirY >= 0); left--, dirY--) {
                                if (state.getFieldButtons()[left][dirY].getChecker() != null) break;
                                else changeFields.add(state.getFieldButtons()[left][dirY]);
                            }
                        } else {
                            if (field.getChecker().isWhiteColour()) direction = -1;
                            else direction = 1;
                            dirY = y + direction; // переписать циклы for в ифы
                            for (right = x + 1; right < BOARD_CONSTANT && (dirY < BOARD_CONSTANT && dirY >= 0); right++, dirY += direction) {
                                if (state.getFieldButtons()[right][dirY].getChecker() == null)
                                    changeFields.add(state.getFieldButtons()[right][dirY]);
                                if (!field.getChecker().isRoyal()) break;
                            }
                            dirY = y + direction;
                            for (left = x - 1; left >= 0 && (dirY < BOARD_CONSTANT && dirY >= 0); left--, dirY += direction) {
                                if (state.getFieldButtons()[left][dirY].getChecker() == null)
                                    changeFields.add(state.getFieldButtons()[left][dirY]);
                                if (!field.getChecker().isRoyal()) break;
                            }
                        }
                        for (Field fields : changeFields) {
                            fields.setState(FieldState.POSSIBLE);
                        }
                        field.setState(FieldState.POSSIBLE);
                    } else {
                        if (field.getState() == FieldState.POSSIBLE) {
                            if (state.getFieldButtons()[lastX][lastY].getChecker().isRoyal()) {
                                field.setChecker(state.getFieldButtons()[lastX][lastY].getChecker());
                                state.getFieldButtons()[lastX][lastY].deleteChecker();
                            } else {
                                if (Math.abs(lastX - x) * Math.abs(lastY - y) == 1) {
                                    field.setChecker(state.getFieldButtons()[lastX][lastY].getChecker());
                                    if (field.getChecker().isWhiteColour() && y == 0) field.getChecker().setRoyal();
                                    if (!field.getChecker().isWhiteColour() && y == 7) field.getChecker().setRoyal();
                                    state.getFieldButtons()[lastX][lastY].deleteChecker();
                                }
                            }
                            changeFields.add(field);
                            changeFields.add(state.getFieldButtons()[lastX][lastY]);
                            for (Field fields : changeFields) {
                                fields.setState(FieldState.EMPTY);
                            }
                            whiteTurn = !whiteTurn;
                        } else field.setState(FieldState.EMPTY);
                    }
                } else {
                    for (Field attackField : attackFields) {
                        attackField.setState(FieldState.ATTACK);
                        changeFields.add(attackField);
                    }
                    if (field.getState() == FieldState.ATTACK && Math.abs(lastX - x) * Math.abs(lastY - y) == 1
                            && state.getFieldButtons()[x + (x - lastX)][y + (y - lastY)].getChecker() == null) {
                        attackFields.remove(field);
                        state.getFieldButtons()[x + (x - lastX)][y + (y - lastY)].setChecker(state.getFieldButtons()[lastX][lastY].getChecker());
                        if (y + (y - lastY) == 0 && state.getFieldButtons()[x + (x - lastX)][y + (y - lastY)].getChecker().isWhiteColour())
                            state.getFieldButtons()[x + (x - lastX)][y + (y - lastY)].getChecker().setRoyal();
                        if (y + (y - lastY) == 7 && !state.getFieldButtons()[x + (x - lastX)][y + (y - lastY)].getChecker().isWhiteColour())
                            state.getFieldButtons()[x + (x - lastX)][y + (y - lastY)].getChecker().setRoyal();
                        state.getFieldButtons()[lastX][lastY].deleteChecker();
                        field.deleteChecker();
                        changeFields.add(field);
                        changeFields.add(state.getFieldButtons()[lastX][lastY]);
                        attackFields = state.attackFields(whiteTurn);
                        if (attackFields.isEmpty()) whiteTurn = !whiteTurn;
                        for (Field fields : changeFields) {
                            fields.setState(FieldState.EMPTY);
                        }
                    } else if (field.getState() != FieldState.ATTACK) field.setState(FieldState.POSSIBLE);
                }
                lastX = x;
                lastY = y;
                state.setActiveField(field);
                stateUpdated();
            }
        };
        return onFieldClick;
    }

    public void setRender(Render render) {
        this.render = render;
    }

    private void stateUpdated() {
        render.render(state);
    }
}
