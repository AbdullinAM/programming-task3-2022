package com.example.russianCheckers.logic;

import com.example.russianCheckers.logic.checkers.Checker;
import com.example.russianCheckers.logic.checkers.StatusChecker;
import com.example.russianCheckers.logic.fieldOptions.FieldInformation;
import com.example.russianCheckers.ui.pages.GameFieldPage;
import com.example.russianCheckers.ui.pages.WelcomePage;

import java.util.ArrayList;
import java.util.List;

import static com.example.russianCheckers.logic.checkers.StatusChecker.*;
import static com.example.russianCheckers.logic.fieldOptions.CellStatus.*;
import static com.example.russianCheckers.ui.settings.Settings.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class CheckersLogic {

    private static Field field;
    private static final String defaultEat = "nothing";
    private static final List<Checker> friendCheckers = new ArrayList<>();
    private static final List<Checker> enemyCheckers = new ArrayList<>();
    private static final List<String> availablePositions = new ArrayList<>();
    private static Checker currentChecker;

    // true - step of friend
    // false(!true) - step of enemy
    private static boolean currentStep = true;

    public static void translateChecker(String position) {
        FieldInformation.getField().put(currentChecker.getPosition(), FREE);
        currentChecker.getChecker().setCenterX(convertPositionToCoordinateX(position));
        currentChecker.getChecker().setCenterY(convertPositionToCoordinateY(position));
        currentChecker.setPosition(position);
        currentChecker.setCenterX(convertPositionToCoordinateX(position));
        currentChecker.setCenterY(convertPositionToCoordinateY(position));
        currentChecker.getChecker().setTranslateZ(4);
        if (currentStep) FieldInformation.getField().put(position, FRIEND_CHECKER);
        else FieldInformation.getField().put(position, ENEMY_CHECKER);
    }

    public static void eatCheckerIfPossible(String position) {
        eatAllCheckersInLine(position, currentChecker.getPosition());
    }

    public static void eatAllCheckersInLine(String pos1, String pos2) {
        StatusChecker statusChecker = currentChecker.getStatusChecker();
        int x1 = convertPositionToCoordinateX(pos1);
        int y1 = convertPositionToCoordinateY(pos1);
        int x2 = convertPositionToCoordinateX(pos2);
        int y2 = convertPositionToCoordinateY(pos2);

        int yStart;
        int yEnd;
        int xMin = min(x1, x2);
        int xMax = max(x1, x2);
        if (xMin == x1) {
            yStart = y1;
            yEnd = y2;
        } else {
            yStart = y2;
            yEnd = y1;
        }

        for (int currentX = xMin; currentX <= xMax; currentX+= 100) {
            int currentY;
            if (yStart < yEnd) currentY = yStart + (currentX - xMin);
            else currentY = yStart - (currentX - xMin);

            String currentPos = convertCoordinateToPosition(currentX, currentY);
            if (statusChecker.equals(CURRENT_CHECKER_WHITE) && FieldInformation.getField().get(currentPos).equals(ENEMY_CHECKER)) {
                FieldInformation.getField().put(currentPos, FREE);
                field.deleteChecker(currentPos);
            } else if(statusChecker.equals(CURRENT_CHECKER_BLACK) && FieldInformation.getField().get(currentPos).equals(FRIEND_CHECKER)) {
                FieldInformation.getField().put(currentPos, FREE);
                field.deleteChecker(currentPos);
            }
        }
    }

    public static void changeSideIfPossibleOrRestart(String position) {

        if (friendCheckers.size() == 0 || enemyCheckers.size() == 0) {
            restartGame();
            WelcomePage.printWelcomePage();
        } else {

            if (FieldInformation.getWhatEat().get(position) != null &&
                    FieldInformation.getWhatEat().get(position).equals(defaultEat)) {
                field.updateAvailableSteps(new ArrayList<>());
                GameFieldPage.updateCurrentStep();
                GameFieldPage.updateCountSteps();
                currentStep = !currentStep;
            } else {
                CheckersLogic.getAvailablePositions().clear();
                CheckersLogic.printAvailableSteps();
                //
                if (currentChecker.isSuperChecker()) {
                    findAvailableEatSuperSteps(position, 1, false);
                    findAvailableEatSuperSteps(position, 2, false);
                    findAvailableEatSuperSteps(position, 3, false);
                    findAvailableEatSuperSteps(position, 4, false);
                } else {
                    findAvailableEatSteps(position, 1);
                    findAvailableEatSteps(position, 2);
                    findAvailableEatSteps(position, 3);
                    findAvailableEatSteps(position, 4);
                }
                if (availablePositions.isEmpty()) {
                    currentStep = !currentStep;
                } else {
                    CheckersLogic.printAvailableSteps();
                }
            }
        }
    }

    public static void findAvailableEatSuperSteps(String position, int direction, boolean isAlreadyPassFirstEnemy) {
        String nextStep = "";
        String nextDoubleStep = "";
        int x = convertPositionToCoordinateX(position);
        int y = convertPositionToCoordinateY(position);
        if (direction == 2) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y - CELL_LENGTH);
        if (direction == 3) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y - CELL_LENGTH);
        if (direction == 2) nextDoubleStep = convertCoordinateToPosition(x + 2 * CELL_LENGTH, y - 2 * CELL_LENGTH);
        if (direction == 3) nextDoubleStep = convertCoordinateToPosition(x - 2 * CELL_LENGTH, y - 2 * CELL_LENGTH);

        if (direction == 1) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y + CELL_LENGTH);
        if (direction == 4) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y + CELL_LENGTH);
        if (direction == 1) nextDoubleStep = convertCoordinateToPosition(x + 2 * CELL_LENGTH, y + 2 * CELL_LENGTH);
        if (direction == 4) nextDoubleStep = convertCoordinateToPosition(x - 2 * CELL_LENGTH, y + 2 * CELL_LENGTH);

        if (FieldInformation.getField().get(position) == null) return;
        if (FieldInformation.getField().get(nextStep) == FRIEND_CHECKER && currentStep) return;
        if (FieldInformation.getField().get(nextStep) == ENEMY_CHECKER && !currentStep) return;
        if (FieldInformation.getField().get(nextStep) == null) return;
        if (FieldInformation.getField().get(nextStep) == FREE && FieldInformation.getField().get(nextDoubleStep) == null) {
            var thisEat = FieldInformation.getWhatEat().get(position);
            if (thisEat == null) thisEat = defaultEat;
            FieldInformation.getWhatEat().put(nextStep, thisEat);
            if (isAlreadyPassFirstEnemy) availablePositions.add(nextStep);
            return;
        }
        if (FieldInformation.getField().get(nextStep) == FREE) {
            var thisEat = FieldInformation.getWhatEat().get(position);
            if (thisEat == null) { thisEat = defaultEat; }
            FieldInformation.getWhatEat().put(nextStep, thisEat);
            if (!thisEat.equals(defaultEat) && isAlreadyPassFirstEnemy) { availablePositions.add(nextStep); }
            findAvailableEatSuperSteps(nextStep, direction, isAlreadyPassFirstEnemy);
        }
        if (FieldInformation.getField().get(nextStep) == ENEMY_CHECKER && FieldInformation.getField().get(nextDoubleStep) == FREE && currentStep) {
            FieldInformation.getWhatEat().put(nextDoubleStep, nextStep);
            availablePositions.add(nextDoubleStep);
            findAvailableEatSuperSteps(nextDoubleStep, direction, true);
        }
        if (FieldInformation.getField().get(nextStep) == FRIEND_CHECKER && FieldInformation.getField().get(nextDoubleStep) == FREE && !currentStep) {
            FieldInformation.getWhatEat().put(nextDoubleStep, nextStep);
            availablePositions.add(nextDoubleStep);
            findAvailableEatSuperSteps(nextDoubleStep, direction, true);
        }
    }

    /**
     * 1 - UP and LEFT
     * 2 - UP and RIGHT
     * 3 - DOWN and LEFT
     * 4 - DOWN and RIGHT
     */
    public static void findAvailableSuperSteps(String position, int direction, boolean isAlreadyPassFirstEnemy) {
        String nextStep = "";
        String nextDoubleStep = "";
        int x = convertPositionToCoordinateX(position);
        int y = convertPositionToCoordinateY(position);
        if (direction == 2) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y - CELL_LENGTH);
        if (direction == 3) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y - CELL_LENGTH);
        if (direction == 2) nextDoubleStep = convertCoordinateToPosition(x + 2 * CELL_LENGTH, y - 2 * CELL_LENGTH);
        if (direction == 3) nextDoubleStep = convertCoordinateToPosition(x - 2 * CELL_LENGTH, y - 2 * CELL_LENGTH);

        if (direction == 1) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y + CELL_LENGTH);
        if (direction == 4) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y + CELL_LENGTH);
        if (direction == 1) nextDoubleStep = convertCoordinateToPosition(x + 2 * CELL_LENGTH, y + 2 * CELL_LENGTH);
        if (direction == 4) nextDoubleStep = convertCoordinateToPosition(x - 2 * CELL_LENGTH, y + 2 * CELL_LENGTH);

        if (FieldInformation.getField().get(position) == null) return;
        if (FieldInformation.getField().get(nextStep) == FRIEND_CHECKER && currentStep) return;
        if (FieldInformation.getField().get(nextStep) == ENEMY_CHECKER && !currentStep) return;
        if (FieldInformation.getField().get(nextStep) == null) return;
        if (FieldInformation.getField().get(nextStep) == FREE && FieldInformation.getField().get(nextDoubleStep) == null) {
            var thisEat = FieldInformation.getWhatEat().get(position);
            if (thisEat == null) thisEat = defaultEat;
            FieldInformation.getWhatEat().put(nextStep, thisEat);
            availablePositions.add(nextStep);
            return;
        }

        if (FieldInformation.getField().get(nextStep) == FREE) {
            var thisEat = FieldInformation.getWhatEat().get(position);
            if (thisEat == null) thisEat = defaultEat;
            FieldInformation.getWhatEat().put(nextStep, thisEat);
            availablePositions.add(nextStep);
            findAvailableSuperSteps(nextStep, direction, isAlreadyPassFirstEnemy);
        }

        if (!isAlreadyPassFirstEnemy) {
            if (FieldInformation.getField().get(nextStep) == ENEMY_CHECKER && FieldInformation.getField().get(nextDoubleStep) == FREE && currentStep) {
                FieldInformation.getWhatEat().put(nextDoubleStep, nextStep);
                availablePositions.add(nextDoubleStep);

                findAvailableSuperSteps(nextDoubleStep, direction, true);
            }
            if (FieldInformation.getField().get(nextStep) == FRIEND_CHECKER && FieldInformation.getField().get(nextDoubleStep) == FREE && !currentStep) {
                FieldInformation.getWhatEat().put(nextDoubleStep, nextStep);
                availablePositions.add(nextDoubleStep);

                findAvailableSuperSteps(nextDoubleStep, direction, true);
            }
        }
    }

    /**
     * 1 - UP and LEFT
     * 2 - UP and RIGHT
     * 3 - DOWN and LEFT
     * 4 - DOWN and RIGHT
     */
    public static void findAvailableEatSteps(String position, int direction) {
        String nextStep = "";
        String nextDoubleStep = "";
        int x = convertPositionToCoordinateX(position);
        int y = convertPositionToCoordinateY(position);

        if (direction == 2) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y - CELL_LENGTH);
        if (direction == 3) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y - CELL_LENGTH);
        if (direction == 2) nextDoubleStep = convertCoordinateToPosition(x + 2 * CELL_LENGTH, y - 2 * CELL_LENGTH);
        if (direction == 3) nextDoubleStep = convertCoordinateToPosition(x - 2 * CELL_LENGTH, y - 2 * CELL_LENGTH);

        if (direction == 1) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y + CELL_LENGTH);
        if (direction == 4) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y + CELL_LENGTH);
        if (direction == 1) nextDoubleStep = convertCoordinateToPosition(x + 2 * CELL_LENGTH, y + 2 * CELL_LENGTH);
        if (direction == 4) nextDoubleStep = convertCoordinateToPosition(x - 2 * CELL_LENGTH, y + 2 * CELL_LENGTH);


        if (currentStep && FieldInformation.getField().get(nextStep) == ENEMY_CHECKER &&
                FieldInformation.getField().get(nextDoubleStep) == FREE) {
            FieldInformation.getWhatEat().put(nextDoubleStep, nextStep);
            availablePositions.add(nextDoubleStep);
        }
        if (!currentStep && FieldInformation.getField().get(nextStep) == FRIEND_CHECKER &&
                FieldInformation.getField().get(nextDoubleStep) == FREE) {
            FieldInformation.getWhatEat().put(nextDoubleStep, nextStep);
            availablePositions.add(nextDoubleStep);
        }
    }

    /**
     * 1 - UP and LEFT
     * 2 - UP and RIGHT
     * 3 - DOWN and LEFT
     * 4 - DOWN and RIGHT
     */
    public static void findAvailableSteps(String position, int direction) {
        String nextStep = "";
        int x = convertPositionToCoordinateX(position);
        int y = convertPositionToCoordinateY(position);
        if (currentStep) {
            if (direction == 2) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y - CELL_LENGTH);
            if (direction == 3) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y - CELL_LENGTH);
        }
        if (!currentStep) {
            if (direction == 1) nextStep = convertCoordinateToPosition(x + CELL_LENGTH, y + CELL_LENGTH);
            if (direction == 4) nextStep = convertCoordinateToPosition(x - CELL_LENGTH, y + CELL_LENGTH);
        }

        if (FieldInformation.getField().get(nextStep) == FREE) {
            FieldInformation.getWhatEat().put(nextStep, defaultEat);
            availablePositions.add(nextStep);
        }
    }

    public static void printAvailableSteps() {
        field.updateAvailableSteps(availablePositions);
        availablePositions.clear();
    }

    public static void setToInactiveOtherCheckers(String position, List<Checker> checkers) {

        checkers
                .stream()
                .filter(checker -> checker.getStatusChecker() == CURRENT_CHECKER_WHITE && !checker.getPosition().equals(position))
                .forEach(checker -> checker.updateStatus(NOTHING_WHITE));

        checkers
                .stream()
                .filter(checker -> checker.getStatusChecker() == CURRENT_CHECKER_BLACK && !checker.getPosition().equals(position))
                .forEach(checker -> checker.updateStatus(NOTHING_BLACK));

    }

    public static void restartGame() {
        FieldInformation.restartFieldInformation();
        currentStep = true;
        friendCheckers.clear();
        enemyCheckers.clear();
        availablePositions.clear();
        currentChecker = null;
        Field.restartField();
        Field field = Field.getInstance();
        CheckersLogic.setField(field);
        field.setFriendCheckers(CheckersLogic.getFriendCheckers());
        field.setEnemyCheckers(CheckersLogic.getEnemyCheckers());
    }

    public static boolean isCurrentStep() {
        return currentStep;
    }

    public static String convertCoordinateToPosition(int centerX, int centerY) {
        int number = centerX / CELL_LENGTH + 1;
        int characterIndex = centerY / CELL_LENGTH;
        String character = switch (characterIndex) {
            case (0) -> "A";
            case (1) -> "B";
            case (2) -> "C";
            case (3) -> "D";
            case (4) -> "E";
            case (5) -> "F";
            case (6) -> "G";
            default -> "H";
        };
        return character + number;
    }

    public static int convertPositionToCoordinateX(String position) {
        return Integer.valueOf(
                Integer.valueOf(String.valueOf(position.charAt(1)))) * CELL_LENGTH - CELL_LENGTH / 2;
    }

    public static int convertPositionToCoordinateY(String position) {
        String character = String.valueOf(position.charAt(0));
        int y = switch (character) {
            case ("A") -> 0;
            case ("B") -> 1;
            case ("C") -> 2;
            case ("D") -> 3;
            case ("E") -> 4;
            case ("F") -> 5;
            case ("G") -> 6;
            case ("H") -> 7;
            default -> throw new IllegalStateException("Unexpected position");
        };
        y = y * CELL_LENGTH + CELL_LENGTH / 2;
        return  y;
    }

    public static List<Checker> getFriendCheckers() {
        return friendCheckers;
    }

    public static List<Checker> getEnemyCheckers() {
        return enemyCheckers;
    }

    public static void setCurrentChecker(Checker checker) {
        currentChecker = checker;
    }

    public static List<String> getAvailablePositions() {
        return availablePositions;
    }

    public static Checker getCurrentChecker() {
        return currentChecker;
    }

    public static void setField(Field field) {
        CheckersLogic.field = field;
    }

}
