package com.example.russianCheckers.logic;

import com.example.russianCheckers.logic.checkers.Checker;
import com.example.russianCheckers.logic.fieldOptions.CellField;
import com.example.russianCheckers.logic.fieldOptions.CellStatus;
import com.example.russianCheckers.ui.pages.GameFieldPage;
import javafx.scene.Group;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.example.russianCheckers.logic.checkers.StatusChecker.NOTHING_BLACK;
import static com.example.russianCheckers.logic.checkers.StatusChecker.NOTHING_WHITE;
import static com.example.russianCheckers.logic.fieldOptions.CellStatus.INACTIVE;
import static com.example.russianCheckers.ui.uiSettings.Settings.*;

@Data
public class Field {

    private final List<CellField> cells = new ArrayList();
    private List<Checker> friendCheckers;
    private List<Checker> enemyCheckers;
    private Group group;
    private final int rangeY = SCREEN_RESOLUTION_Y;
    private final int rangeX = SCREEN_RESOLUTION_X;
    private final int step = CELL_LENGTH;
    private static Field instance = new Field();
    public static Field getInstance() {
        return instance;
    }

    public static void restartField() {
        instance = new Field();
    }

    public void updateAvailableSteps(List<String> availableCells) {
        if (availableCells.isEmpty()) {
            cells.forEach(cell -> cell.updateCell(INACTIVE));
        } else {
            cells.forEach(it -> {
                for (String cell : availableCells) {
                    if (it.getPosition().equals(cell)) {
                        it.updateCell(CellStatus.ACTIVE);
                        return;
                    }
                    it.updateCell(INACTIVE);
                }
            });
        }
    }

    public void printInitCheckers() {
        int offsetY = 0;
        boolean lineOffset = false;
        int countLines = 0;
        while (offsetY < rangeY) {
            int offsetX = lineOffset ? 0 : step;
            if (countLines < 3) {
                while (offsetX < rangeX) {
                    Checker checker = new Checker(offsetX + step / 2, offsetY + step / 2);
                    checker.updateStatus(NOTHING_BLACK);
                    offsetX += step * 2;
                    enemyCheckers.add(checker);
                }
            }
            if (countLines > 4) {
                while (offsetX < rangeX) {
                    Checker checker = new Checker(offsetX + step / 2, offsetY + step / 2);
                    checker.updateStatus(NOTHING_WHITE);
                    offsetX += step * 2;
                    friendCheckers.add(checker);
                }
            }
            offsetY += step;
            lineOffset = !lineOffset;
            countLines++;
        }
        friendCheckers.forEach(it -> group.getChildren().add(it.getChecker()));
        enemyCheckers.forEach(it -> group.getChildren().add(it.getChecker()));
    }

    public void deleteChecker(String position) {

        CheckersLogic.getFriendCheckers().stream()
                .filter(checker -> checker.getPosition().equals(position))
                .forEach(checker -> {
                    GameFieldPage.updateWhiteCheckersAlive();
                    group.getChildren().remove(checker.getChecker());
                });
        CheckersLogic.getEnemyCheckers().stream()
                .filter(checker -> checker.getPosition().equals(position))
                .forEach(checker -> {
                    GameFieldPage.updateBlackCheckersAlive();
                    group.getChildren().remove(checker.getChecker());
                });

        for (int i = 0; i < CheckersLogic.getFriendCheckers().size(); i++) {
            if (CheckersLogic.getFriendCheckers().get(i).getPosition().equals(position)) {
                CheckersLogic.getFriendCheckers().remove(CheckersLogic.getFriendCheckers().get(i));
            }
        }
        for (int i = 0; i < CheckersLogic.getEnemyCheckers().size(); i++) {
            if (CheckersLogic.getEnemyCheckers().get(i).getPosition().equals(position)) {
                CheckersLogic.getEnemyCheckers().remove(CheckersLogic.getEnemyCheckers().get(i));
            }
        }
    }

    public void printField() {
        int offsetY = 0;
        boolean lineOffset = false;
        while (offsetY < rangeY) {
            int offsetX = lineOffset? 0 : step;
            while (offsetX < rangeX) {
                CellField cellField = new CellField(offsetX, offsetY);
                cellField.updateCell(INACTIVE);
                cells.add(cellField);
                offsetX += step * 2;
            }
            offsetY += step;
            lineOffset = !lineOffset;
        }
        cells.forEach(it -> group.getChildren().add(it.getCell()));
    }

    public void setFriendCheckers(List<Checker> friendCheckersList) {
        friendCheckers = friendCheckersList;
    }
    public void setEnemyCheckers(List<Checker> enemyCheckersList) {
        enemyCheckers = enemyCheckersList;
    }
    public void setGroup(Group root) {
        group = root;
    }

}
