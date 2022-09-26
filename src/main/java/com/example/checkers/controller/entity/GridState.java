package com.example.checkers.controller.entity;

import java.util.ArrayList;

public class GridState {
    private Field[][] fieldButtons;
    private Field activeField;

    public static int BOARD_CONSTANT = 8;

    int counter = 0;

    public GridState() {
        this.fieldButtons = new Field[BOARD_CONSTANT][BOARD_CONSTANT];

        for (int j = 0; j < BOARD_CONSTANT; j++) {
            counter++;
            for (int i = 0; i < BOARD_CONSTANT; i++) {
                fieldButtons[i][j] = new Field();
                if (1 == counter % 2) {
                    if (counter > 45)
                        fieldButtons[i][j].setChecker(new Checker(Checker.Colour.WHITE));
                    if (counter < 27)
                        fieldButtons[i][j].setChecker(new Checker(Checker.Colour.BLACK));
                }
                counter++;
            }
        }
    }

    public void setActiveField(Field activeField) {
        this.activeField = activeField;
    }

    public Field getActiveField() {
        return activeField;
    }

    public Field[][] getFieldButtons() {
        return fieldButtons;
    }

    public ArrayList<Field> attackFields(boolean white) {
        int left;
        int right;
        int up;
        int down;
        ArrayList<Field> attackFields = new ArrayList<>();
        for (int j = 0; j < BOARD_CONSTANT; j++)
            for (int i = 0; i < BOARD_CONSTANT; i++) {
                if (fieldButtons[i][j].getChecker() != null && fieldButtons[i][j].getChecker().isWhiteColour() == white) {
                    if (fieldButtons[i][j].getChecker().isRoyal()) {
                        for (right = i + 1, down = j + 1; right < BOARD_CONSTANT && right > 0 && down > 0 && down < BOARD_CONSTANT; right++, down++) {
                            if (fieldButtons[right - 1][down - 1].getChecker() != null) {
                                if (fieldButtons[right][down].getChecker() != null && fieldButtons[right - 1][down - 1] != fieldButtons[i][j])
                                    break;
                                if (fieldButtons[right][down].getChecker() == null &&
                                        fieldButtons[right - 1][down - 1].getChecker().isWhiteColour() != white) {
                                    attackFields.add(fieldButtons[right - 1][down - 1]);
                                }
                            }
                        }
                        for (right = i + 1, up = j - 1; right < BOARD_CONSTANT && right > 0 && up >= 0 && up < BOARD_CONSTANT - 1; right++, up--) {
                            if (fieldButtons[right - 1][up + 1].getChecker() != null) {
                                if (fieldButtons[right][up].getChecker() != null && fieldButtons[right - 1][up + 1] != fieldButtons[i][j])
                                    break;
                                if (fieldButtons[right][up].getChecker() == null &&
                                        fieldButtons[right - 1][up + 1].getChecker().isWhiteColour() != white)
                                    attackFields.add(fieldButtons[right - 1][up + 1]);
                            }

                        }
                        for (left = i - 1, up = j - 1; left < BOARD_CONSTANT - 1 && left >= 0 && up >= 0 && up < BOARD_CONSTANT - 1; left--, up--) {
                            if (fieldButtons[left + 1][up + 1].getChecker() != null) {
                                if (fieldButtons[left][up].getChecker() != null && fieldButtons[left + 1][up + 1] != fieldButtons[i][j])
                                    break;
                                if (fieldButtons[left][up].getChecker() == null &&
                                        fieldButtons[left + 1][up + 1].getChecker().isWhiteColour() != white)
                                    attackFields.add(fieldButtons[left + 1][up + 1]);
                            }
                        }
                        for (left = i - 1, down = j + 1; left < BOARD_CONSTANT - 1 && left >= 0 && down > 0 && down < BOARD_CONSTANT; left--, down++) {
                            if (fieldButtons[left + 1][down - 1].getChecker() != null) {
                                if (fieldButtons[left][down].getChecker() != null && fieldButtons[left + 1][down - 1] != fieldButtons[i][j])
                                    break;
                                if (fieldButtons[left][down].getChecker() == null &&
                                        fieldButtons[left + 1][down - 1].getChecker().isWhiteColour() != white)
                                    attackFields.add(fieldButtons[left + 1][down - 1]);
                            }
                        }
                    } else {
                        if (i < 6 && j < 6 && fieldButtons[i + 1][j + 1].getChecker() != null
                                && fieldButtons[i + 1][j + 1].getChecker().isWhiteColour() != white
                                && fieldButtons[i + 2][j + 2].getChecker() == null) {
                            attackFields.add(fieldButtons[i + 1][j + 1]);
                        }
                        if (i < 6 && j > 1 && fieldButtons[i + 1][j - 1].getChecker() != null
                                && fieldButtons[i + 1][j - 1].getChecker().isWhiteColour() != white
                                && fieldButtons[i + 2][j - 2].getChecker() == null) {
                            attackFields.add(fieldButtons[i + 1][j - 1]);
                        }
                        if (i > 1 && j > 1 && fieldButtons[i - 1][j - 1].getChecker() != null
                                && fieldButtons[i - 1][j - 1].getChecker().isWhiteColour() != white
                                && fieldButtons[i - 2][j - 2].getChecker() == null) {
                            attackFields.add(fieldButtons[i - 1][j - 1]);
                        }
                        if (i > 1 && j < 6 && fieldButtons[i - 1][j + 1].getChecker() != null
                                && fieldButtons[i - 1][j + 1].getChecker().isWhiteColour() != white
                                && fieldButtons[i - 2][j + 2].getChecker() == null) {
                            attackFields.add(fieldButtons[i - 1][j + 1]);
                        }
                    }
                }
            }
        return attackFields;
    }
}
