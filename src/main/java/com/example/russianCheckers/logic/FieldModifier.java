package com.example.russianCheckers.logic;

import com.example.russianCheckers.ui.FieldUI;
import com.example.russianCheckers.ui.Settings;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Data
public class FieldModifier {

    private static String[] superPositionsWhite = new String[] { "A2", "A4", "A6", "A8" };
    private static String[] superPositionsBlack = new String[] { "H1", "H3", "H5", "H7" };
    private static boolean unlockCurrentStep = true;
    private static boolean isWhiteStep = true;
    private static Checker selectedChecker;
    private static String currentPosition;
    private static List<String> availablePositions = new ArrayList<>();

    private static Supplier<String> up = () ->  String.valueOf((char) (((int) currentPosition.charAt(0)) + 1)) + currentPosition.charAt(1);
    private static Supplier<String> down = () -> String.valueOf((char) (((int) currentPosition.charAt(0)) - 1)) + currentPosition.charAt(1);
    private static Supplier<String> left = () -> currentPosition.charAt(0) + String.valueOf(Character.getNumericValue(currentPosition.charAt(1)) + 1);
    private static Supplier<String> right = () -> currentPosition.charAt(0) + String.valueOf(Character.getNumericValue(currentPosition.charAt(1)) - 1);

    private static void availablePositionsInSelectedDirectionSuperChecker(Supplier<String> vertical, Supplier<String> horizontal) {
        currentPosition = selectedChecker.getPosition();
        boolean isPreviousBlack = false;
        boolean isPreviousWhite = false;
        boolean isAlreadyAte = false;
        currentPosition = vertical.get();
        currentPosition = horizontal.get();
        while (insideDesk()) {
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE) availablePositions.add(currentPosition);
            if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && (isPreviousBlack || isPreviousWhite)) return;
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
            if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
            currentPosition = vertical.get();
            currentPosition = horizontal.get();
        }
    }

    private static void availablePositionsInSelectedDirection(Supplier<String> vertical, Supplier<String> horizontal, CheckerStatus enemy, boolean lockBeckStep) {
        currentPosition = selectedChecker.getPosition();
        currentPosition = vertical.get();
        currentPosition = horizontal.get();
        if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && !lockBeckStep)
            availablePositions.add(currentPosition);
        else if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == enemy) {
            currentPosition = vertical.get();
            currentPosition = horizontal.get();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                availablePositions.add(currentPosition);
        }
    }

    public static void findAvailableSteps() {
        FieldModifier.availablePositions.clear();

        if (selectedChecker == null) return;

        if (selectedChecker.isSuperChecker()) {
            availablePositionsInSelectedDirectionSuperChecker(up, left);
            availablePositionsInSelectedDirectionSuperChecker(up, right);
            availablePositionsInSelectedDirectionSuperChecker(down, left);
            availablePositionsInSelectedDirectionSuperChecker(down, right);
            return;
        }
        if (isWhiteStep) {
            availablePositionsInSelectedDirection(down, left, CheckerStatus.BLACK, false);
            availablePositionsInSelectedDirection(down, right, CheckerStatus.BLACK, false);
            availablePositionsInSelectedDirection(up, left, CheckerStatus.BLACK, true);
            availablePositionsInSelectedDirection(up, right, CheckerStatus.BLACK, true);
            return;
        }
        availablePositionsInSelectedDirection(down, left, CheckerStatus.WHITE, true);
        availablePositionsInSelectedDirection(down, right, CheckerStatus.WHITE, true);
        availablePositionsInSelectedDirection(up, left, CheckerStatus.WHITE, false);
        availablePositionsInSelectedDirection(up, right, CheckerStatus.WHITE, false);
    }

    private static void findAvailableStepsAfterEat() {
        if (selectedChecker.isSuperChecker()) {
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(up, right);
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(up, left);
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(down, right);
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(down, left);
            return;
        }

        if (isWhiteStep) {
            availablePositionsSelectedDirectionAfterEat(down, left, CheckerStatus.BLACK);
            availablePositionsSelectedDirectionAfterEat(down, right, CheckerStatus.BLACK);
            availablePositionsSelectedDirectionAfterEat(up, left, CheckerStatus.BLACK);
            availablePositionsSelectedDirectionAfterEat(up, right, CheckerStatus.BLACK);
            return;
        }

        availablePositionsSelectedDirectionAfterEat(down, left, CheckerStatus.WHITE);
        availablePositionsSelectedDirectionAfterEat(down, right, CheckerStatus.WHITE);
        availablePositionsSelectedDirectionAfterEat(up, left, CheckerStatus.WHITE);
        availablePositionsSelectedDirectionAfterEat(up, right, CheckerStatus.WHITE);
    }

    public static void tryChangeSide(String positionBefore, String positionAfter) {

        List<String> positions = calculateRangePositions(positionBefore, positionAfter);

        if (isWhiteStep && positions.stream().map(pos -> Field.getCells().get(pos)).anyMatch(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK)) {

            eat(positions);
            availablePositions.clear();
            findAvailableStepsAfterEat();
            if (!availablePositions.isEmpty()) unlockCurrentStep = false;
            else {
                unlockCurrentStep = true;
                isWhiteStep = !isWhiteStep;
            }
            return;
        }

        if (!isWhiteStep && positions.stream().map(pos -> Field.getCells().get(pos)).anyMatch(checker -> checker.getCheckerStatus() == CheckerStatus.WHITE)) {
            eat(positions);
            availablePositions.clear();
            findAvailableStepsAfterEat();
            if (!availablePositions.isEmpty()) unlockCurrentStep = false;
            else {
                unlockCurrentStep = true;
                isWhiteStep = !isWhiteStep;
            }
            return;
        }

        isWhiteStep = !isWhiteStep;
        availablePositions.clear();
        unlockCurrentStep = true;
    }

    private static void availablePositionsInSelectedDirectionSuperCheckerAfterEat(Supplier<String> vertical, Supplier<String> horizontal) {
        currentPosition = selectedChecker.getPosition();
        boolean isPreviousBlack = false;
        boolean isPreviousWhite = false;
        boolean isAlreadyAte = false;
        currentPosition = vertical.get();
        currentPosition = horizontal.get();
        while (insideDesk()) {
            if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && (isPreviousBlack || isPreviousWhite)) return;
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isAlreadyAte) availablePositions.add(currentPosition);
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
            if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
            if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
            currentPosition = vertical.get();
            currentPosition = horizontal.get();
        }
    }

    private static void availablePositionsSelectedDirectionAfterEat(Supplier<String> vertical, Supplier<String> horizontal, CheckerStatus enemy) {
        currentPosition = selectedChecker.getPosition();
        currentPosition = vertical.get();
        currentPosition = horizontal.get();
        if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == enemy) {
            currentPosition = vertical.get();
            currentPosition = horizontal.get();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                availablePositions.add(currentPosition);
        }
    }

    private static void eat(List<String> positions) {
        positions.stream().map(pos -> Field.getCells().get(pos)).forEach(checker -> checker.setCheckerStatus(CheckerStatus.FREE));
    }
    private static List<String> calculateRangePositions(String positionOne, String positionTwo) {
        List<String> result = new ArrayList<>();
        int x1 = getXFromPosition(positionOne);
        int y1 = getYFromPosition(positionOne);
        int x2 = getXFromPosition(positionTwo);
        int y2 = getYFromPosition(positionTwo);
        if (x1 < x2 && y1 > y2) {
            for (int dx = x1 + Settings.CELL_LENGTH; dx < x2; dx += Settings.CELL_LENGTH) {
                y1 -= Settings.CELL_LENGTH;
                String curPos = coordinatesToPosition(dx, y1);
                result.add(curPos);
            }
        } else  if (x1 > x2 && y1 < y2) {
            for (int dx = x1 - Settings.CELL_LENGTH; dx > x2; dx -= Settings.CELL_LENGTH) {
                y1 += Settings.CELL_LENGTH;
                String curPos = coordinatesToPosition(dx, y1);
                result.add(curPos);
            }
        } else if (x1 < x2 && y1 < y2) {
            for (int dx = x1 + Settings.CELL_LENGTH; dx < x2; dx += Settings.CELL_LENGTH) {
                y1 += Settings.CELL_LENGTH;
                String curPos = coordinatesToPosition(dx, y1);
                result.add(curPos);
            }
        } else if (x1 > x2 && y1 > y2) {
            for (int dx = x1 - Settings.CELL_LENGTH; dx > x2; dx -= Settings.CELL_LENGTH) {
                y1 -= Settings.CELL_LENGTH;
                String curPos = coordinatesToPosition(dx, y1);
                result.add(curPos);
            }
        }
        return result;
    }

    public static boolean checkNewSuperChecker() {
        if (selectedChecker.getCheckerStatus() == CheckerStatus.WHITE) {
            if (Arrays.stream(superPositionsWhite).anyMatch(pos -> pos.equals(selectedChecker.getPosition()))) {
                selectedChecker.setSuperChecker(true);
                return true;
            }
        } else {
            if (Arrays.stream(superPositionsBlack).anyMatch(pos -> pos.equals(selectedChecker.getPosition()))) {
                selectedChecker.setSuperChecker(true);
                return true;
            }
        }
        return false;
    }

    private static boolean insideDesk() {
        return
                Character.getNumericValue(currentPosition.charAt(1)) > 0 &&
                Character.getNumericValue(currentPosition.charAt(1)) < 9 &&
                ((int) currentPosition.charAt(0)) > 64 &&
                ((int) currentPosition.charAt(0)) < 73;
    }

    public static void tryFinalizeGame() {
        // one side has no available positions or no checkers
        if (
                Field.getCells().entrySet().stream().noneMatch(checker -> checker.getValue().getCheckerStatus() == CheckerStatus.BLACK) ||
                Field.getCells().entrySet().stream().noneMatch(checker -> checker.getValue().getCheckerStatus() == CheckerStatus.WHITE) ||
                !hasAnyAvailableStep()) {
            FieldUI.getFieldUI().renderEndGamePage();
        }
    }

    public static boolean hasAnyAvailableStep() {
        boolean isUnlockCurrentStep = FieldModifier.unlockCurrentStep;
        boolean isWhiteStep = FieldModifier.isWhiteStep;
        String currentPosition = selectedChecker.getPosition();
        List<String> availablePositions = new ArrayList<>(FieldModifier.availablePositions);

        boolean flag =
                (Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.WHITE).anyMatch(checker -> {
                   FieldModifier.isWhiteStep = true;
                   FieldModifier.unlockCurrentStep = true;
                   FieldModifier.selectedChecker = checker;
                   findAvailableSteps();
                   return (FieldModifier.availablePositions.size() > 0);
                }) &&
                Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK).anyMatch(checker -> {
                    FieldModifier.isWhiteStep = false;
                    FieldModifier.unlockCurrentStep = true;
                    FieldModifier.selectedChecker = checker;
                    findAvailableSteps();
                    return (FieldModifier.availablePositions.size() > 0);
                }) );
        FieldModifier.unlockCurrentStep = isUnlockCurrentStep;
        FieldModifier.isWhiteStep = isWhiteStep;
        FieldModifier.selectedChecker = Field.getCells().get(currentPosition);
        FieldModifier.availablePositions = new ArrayList<>(availablePositions);
        return flag;
    }

    public static void restartGame() {
        Field.refreshField();
        FieldModifier.selectedChecker = null;
        FieldModifier.availablePositions.clear();
        FieldModifier.currentPosition = null;
        FieldModifier.isWhiteStep = true;
        FieldModifier.unlockCurrentStep = true;
    }

    public static int getXFromPosition(String position) {
        return Character.getNumericValue(position.charAt(1)) * Settings.CELL_LENGTH;
    }

    public static int getYFromPosition(String position) {
        return (position.charAt(0) - 64) * Settings.CELL_LENGTH;
    }

    public static String coordinatesToPosition(double x, double y) {
        return ((char) (y / Settings.CELL_LENGTH + 64)) + String.valueOf(((int) x / Settings.CELL_LENGTH));
    }
    public static void setSelectedChecker(Checker selectedChecker) {
        FieldModifier.selectedChecker = selectedChecker;
    }

    public static Checker getSelectedChecker() {
        return FieldModifier.selectedChecker;
    }

    public static List<String> getAvailableSteps() {
        return FieldModifier.availablePositions;
    }

    public static boolean isWhiteStep() {
        return FieldModifier.isWhiteStep;
    }

    public static void setIsWhiteStep(boolean isWhiteStep) {
        FieldModifier.isWhiteStep = isWhiteStep;
    }

    public static boolean isUnlockCurrentStep() {
        return FieldModifier.unlockCurrentStep;
    }

}
