package com.example.russianCheckers.logic;

import com.example.russianCheckers.ui.FieldUI;
import com.example.russianCheckers.ui.Settings;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Data
public class FieldModifier {

    private static String[] superPositionsWhite = new String[] { "A2", "A4", "A6", "A8" };
    private static String[] superPositionsBlack = new String[] { "H1", "H3", "H5", "H7" };
    private static boolean unlockCurrentStep = true;
    private static boolean isWhiteStep = true;
    private static Checker selectedChecker;
    private static String currentPosition;
    private static List<String> availablePositions = new ArrayList<>();
    private static List<String> availablePositionsToClickToCheckers = new ArrayList<>();

    private static Function<String, String> up = (pos) ->  String.valueOf((char) (((int) pos.charAt(0)) + 1)) + pos.charAt(1);
    private static Function<String, String> down = (pos) -> String.valueOf((char) (((int) pos.charAt(0)) - 1)) + pos.charAt(1);
    private static Function<String, String> left = (pos) -> pos.charAt(0) + String.valueOf(Character.getNumericValue(pos.charAt(1)) + 1);
    private static Function<String, String> right = (pos) -> pos.charAt(0) + String.valueOf(Character.getNumericValue(pos.charAt(1)) - 1);

    private static void availablePositionsInSelectedDirectionSuperChecker(Function<String, String> vertical, Function<String, String> horizontal, List<String> result, Checker checker) {
        String pos = checker.getPosition();
        boolean isPreviousBlack = false;
        boolean isPreviousWhite = false;
        boolean isAlreadyAte = false;
        pos = vertical.apply(pos);
        pos = horizontal.apply(pos);
        while (insideDesk(pos)) {
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE) result.add(pos);
            if (Field.getCells().get(pos).getCheckerStatus() != CheckerStatus.FREE && (isPreviousBlack || isPreviousWhite)) return;
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
            if (Field.getCells().get(pos).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && checker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; result.add(pos); }
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && checker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; result.add(pos); }
            pos = vertical.apply(pos);
            pos = horizontal.apply(pos);
        }
    }

    private static void availablePositionsInSelectedDirection(Function<String, String> vertical, Function<String, String> horizontal, CheckerStatus enemy, boolean lockBeckStep, List<String> result, Checker checker) {
        String pos = checker.getPosition();
        pos = vertical.apply(pos);
        pos = horizontal.apply(pos);
        if (Field.getCells().get(pos) != null && Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE && !lockBeckStep)
            result.add(pos);
        else if (Field.getCells().get(pos) != null && Field.getCells().get(pos).getCheckerStatus() == enemy) {
            pos = vertical.apply(pos);
            pos = horizontal.apply(pos);
            if (Field.getCells().get(pos) != null && Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE)
                result.add(pos);
        }
    }

    public static List<String> findAvailableSteps(Checker checker, boolean isWhiteStep) {
        List<String> result = new ArrayList<>();

        if (checker.isSuperChecker()) {
            availablePositionsInSelectedDirectionSuperChecker(up, left, result, checker);
            availablePositionsInSelectedDirectionSuperChecker(up, right, result, checker);
            availablePositionsInSelectedDirectionSuperChecker(down, left, result, checker);
            availablePositionsInSelectedDirectionSuperChecker(down, right, result, checker);
            return result;
        }
        if (isWhiteStep) {
            availablePositionsInSelectedDirection(down, left, CheckerStatus.BLACK, false, result, checker);
            availablePositionsInSelectedDirection(down, right, CheckerStatus.BLACK, false, result, checker);
            availablePositionsInSelectedDirection(up, left, CheckerStatus.BLACK, true, result, checker);
            availablePositionsInSelectedDirection(up, right, CheckerStatus.BLACK, true, result, checker);
            return result;
        }
        availablePositionsInSelectedDirection(down, left, CheckerStatus.WHITE, true, result, checker);
        availablePositionsInSelectedDirection(down, right, CheckerStatus.WHITE, true, result, checker);
        availablePositionsInSelectedDirection(up, left, CheckerStatus.WHITE, false, result, checker);
        availablePositionsInSelectedDirection(up, right, CheckerStatus.WHITE, false, result, checker);
        return result;
    }

    public static List<String> findAvailableStepsAfterEat(Checker checker, boolean isWhiteStep) {
        List<String> result = new ArrayList<>();
        if (checker.isSuperChecker()) {
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(up, right, result, checker);
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(up, left, result, checker);
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(down, right, result, checker);
            availablePositionsInSelectedDirectionSuperCheckerAfterEat(down, left, result, checker);
            return result;
        }

        if (isWhiteStep) {
            availablePositionsSelectedDirectionAfterEat(down, left, CheckerStatus.BLACK, result, checker);
            availablePositionsSelectedDirectionAfterEat(down, right, CheckerStatus.BLACK, result, checker);
            availablePositionsSelectedDirectionAfterEat(up, left, CheckerStatus.BLACK, result, checker);
            availablePositionsSelectedDirectionAfterEat(up, right, CheckerStatus.BLACK, result, checker);
            return result;
        }

        availablePositionsSelectedDirectionAfterEat(down, left, CheckerStatus.WHITE, result, checker);
        availablePositionsSelectedDirectionAfterEat(down, right, CheckerStatus.WHITE, result, checker);
        availablePositionsSelectedDirectionAfterEat(up, left, CheckerStatus.WHITE, result, checker);
        availablePositionsSelectedDirectionAfterEat(up, right, CheckerStatus.WHITE, result, checker);
        return result;
    }

    public static void tryChangeSide(String positionBefore, String positionAfter) {

        List<String> positions = calculateRangePositions(positionBefore, positionAfter);

        if (isWhiteStep && positions.stream().map(pos -> Field.getCells().get(pos)).anyMatch(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK)) {

            eat(positions);
            availablePositions.clear();
            availablePositions.addAll(findAvailableStepsAfterEat(selectedChecker, isWhiteStep));
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
            availablePositions.addAll(findAvailableStepsAfterEat(selectedChecker, isWhiteStep));
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

    private static void availablePositionsInSelectedDirectionSuperCheckerAfterEat(Function<String, String> vertical, Function<String, String> horizontal, List<String> result, Checker checker) {
        String pos = checker.getPosition();
        boolean isPreviousBlack = false;
        boolean isPreviousWhite = false;
        boolean isAlreadyAte = false;
        pos = vertical.apply(pos);
        pos = horizontal.apply(pos);
        while (insideDesk(pos)) {
            if (Field.getCells().get(pos).getCheckerStatus() != CheckerStatus.FREE && (isPreviousBlack || isPreviousWhite)) return;
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE && isAlreadyAte) result.add(pos);
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
            if (Field.getCells().get(pos).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && checker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; result.add(pos); }
            if (Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && checker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; result.add(pos); }
            pos = vertical.apply(pos);
            pos = horizontal.apply(pos);
        }
    }

    private static void availablePositionsSelectedDirectionAfterEat(Function<String, String> vertical, Function<String, String> horizontal, CheckerStatus enemy, List<String> result, Checker checker) {
        String pos = checker.getPosition();
        pos = vertical.apply(pos);
        pos = horizontal.apply(pos);
        if (Field.getCells().get(pos) != null && Field.getCells().get(pos).getCheckerStatus() == enemy) {
            pos = vertical.apply(pos);
            pos = horizontal.apply(pos);
            if (Field.getCells().get(pos) != null && Field.getCells().get(pos).getCheckerStatus() == CheckerStatus.FREE)
                result.add(pos);
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

    private static boolean insideDesk(String pos) {
        return
                Character.getNumericValue(pos.charAt(1)) > 0 &&
                Character.getNumericValue(pos.charAt(1)) < 9 &&
                ((int) pos.charAt(0)) > 64 &&
                ((int) pos.charAt(0)) < 73;
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

    public static boolean isNeedOnlyEat() {
        AtomicBoolean takeAllPos = new AtomicBoolean(false);
        if (isWhiteStep) {
            Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.WHITE).forEach(checker -> {
                findAvailableSteps(checker, true).forEach(pos -> {
                    if (calculateRangePositions(checker.getPosition(), pos).stream()
                            .anyMatch(btwPos -> Field.getCells().get(btwPos).getCheckerStatus() == CheckerStatus.BLACK)) {
                        takeAllPos.set(true);
                    }
                });
            });
        } else {
            Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK).forEach(checker -> {
                findAvailableSteps(checker, false).forEach(pos -> {
                    if (calculateRangePositions(checker.getPosition(), pos).stream()
                            .anyMatch(btwPos -> Field.getCells().get(btwPos).getCheckerStatus() == CheckerStatus.WHITE)) {
                        takeAllPos.set(true);
                    }
                });
            });
        }
        return takeAllPos.get();
    }

    public static List<String> findAllAvailablePositionsToStep() {
        List<String> result = new ArrayList<>();

        AtomicBoolean takeAllPos = new AtomicBoolean(true);
        if (isWhiteStep) {
            Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.WHITE).forEach(checker -> {
                findAvailableSteps(checker, true).forEach(pos -> {
                            if (calculateRangePositions(checker.getPosition(), pos).stream()
                                    .anyMatch(btwPos -> Field.getCells().get(btwPos).getCheckerStatus() == CheckerStatus.BLACK)) {
                                takeAllPos.set(false);
                                result.add(checker.getPosition());
                            }
                        });
            });
            if (takeAllPos.get()) {
                result.addAll(Field
                        .getCells()
                        .values()
                        .stream()
                        .filter(checker -> checker.getCheckerStatus() == CheckerStatus.WHITE).map(Checker::getPosition)
                        .toList());
            }
        } else {
            Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK).forEach(checker -> {
                findAvailableSteps(checker, false).forEach(pos -> {
                    if (calculateRangePositions(checker.getPosition(), pos).stream()
                            .anyMatch(btwPos -> Field.getCells().get(btwPos).getCheckerStatus() == CheckerStatus.WHITE)) {
                        takeAllPos.set(false);
                        result.add(checker.getPosition());
                    }
                });
            });
            if (takeAllPos.get()) {
                result.addAll(Field
                        .getCells()
                        .values()
                        .stream()
                        .filter(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK).map(Checker::getPosition)
                        .toList());
            }
        }
        return result;
    }

    public static boolean hasAnyAvailableStep() {
        return
                (Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.WHITE).anyMatch(checker -> findAvailableSteps(checker, true).size() > 0) &&
                Field.getCells().values().stream().filter(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK).anyMatch(checker -> findAvailableSteps(checker, false).size() > 0));
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

    public static List<String> getAvailablePositionsToClickToCheckers() {
        return FieldModifier.availablePositionsToClickToCheckers;
    }

    public static void setAvailablePositions(List<String> positions) {
        FieldModifier.availablePositions.clear();
        FieldModifier.availablePositions.addAll(positions);
    }

}
