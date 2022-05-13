package com.example.russianCheckers.logic;

import com.example.russianCheckers.ui.Settings;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class FieldModifier {

    private static String[] superPositions = new String[] {"A2", "A4", "A6", "A8", "H1", "H3", "H5", "H7"};
    private static boolean unlockCurrentStep = true;
    private static boolean isWhiteStep = true;
    private static Checker selectedChecker;
    private static String currentPosition;

    private static List<String> availablePositions = new ArrayList<>();


    public static void findAvailableSteps() {
        FieldModifier.availablePositions.clear();

        if (selectedChecker == null) return;

        if (selectedChecker.isSuperChecker()) {
            currentPosition = selectedChecker.getPosition();
            boolean isPreviousBlack = false;
            boolean isPreviousWhite = false;
            boolean isAlreadyAte = false;
            up();
            left();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                up();
                left();
            }
            currentPosition = selectedChecker.getPosition();
            isPreviousBlack = false;
            isPreviousWhite = false;
            isAlreadyAte = false;
            up();
            right();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                up();
                right();
            }
            currentPosition = selectedChecker.getPosition();
            isPreviousBlack = false;
            isPreviousWhite = false;
            isAlreadyAte = false;
            down();
            left();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                down();
                left();
            }
            currentPosition = selectedChecker.getPosition();
            isPreviousBlack = false;
            isPreviousWhite = false;
            isAlreadyAte = false;
            down();
            right();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                down();
                right();
            }
            return;
        }













        currentPosition = selectedChecker.getPosition();
        if (isWhiteStep) {
            down();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                availablePositions.add(currentPosition);
            else if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                down();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            down();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                availablePositions.add(currentPosition);
            else if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                down();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }

            //
            currentPosition = selectedChecker.getPosition();
            up();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                up();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            up();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                up();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
        }

        if (!isWhiteStep) {
            up();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                availablePositions.add(currentPosition);
            else if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                up();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            up();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                availablePositions.add(currentPosition);
            else if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                up();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }

            //
            currentPosition = selectedChecker.getPosition();
            down();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                down();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            down();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                down();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
        }
    }

    public static void tryChangeSide(String positionBefore, String positionAfter) {

        List<String> positions = calculateRangePositions(positionBefore, positionAfter);

        if (positions.isEmpty()) {
            isWhiteStep = !isWhiteStep;
            availablePositions.clear();
            unlockCurrentStep = true;
            return;
        }

        if (isWhiteStep && positions.stream().map(pos -> Field.getCells().get(pos)).anyMatch(checker -> checker.getCheckerStatus() == CheckerStatus.BLACK)) {
            eat(positions);
            availablePositions.clear();
            findAvailableStepsAfterEat();
            if (!availablePositions.isEmpty()) unlockCurrentStep = false;
            else {
                unlockCurrentStep = true;
                isWhiteStep = !isWhiteStep;
            }
        }

        else if (!isWhiteStep && positions.stream().map(pos -> Field.getCells().get(pos)).anyMatch(checker -> checker.getCheckerStatus() == CheckerStatus.WHITE)) {

            eat(positions);
            availablePositions.clear();
            findAvailableStepsAfterEat();
            if (!availablePositions.isEmpty()) unlockCurrentStep = false;
            else {
                unlockCurrentStep = true;
                isWhiteStep = !isWhiteStep();
            }
        }
    }
    private static void findAvailableStepsAfterEat() {

        if (selectedChecker.isSuperChecker()) {
            currentPosition = selectedChecker.getPosition();
            boolean isPreviousBlack = false;
            boolean isPreviousWhite = false;
            boolean isAlreadyAte = false;
            up();
            left();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isAlreadyAte) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                up();
                left();
            }
            currentPosition = selectedChecker.getPosition();
            isPreviousBlack = false;
            isPreviousWhite = false;
            isAlreadyAte = false;
            up();
            right();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isAlreadyAte) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                up();
                right();
            }
            currentPosition = selectedChecker.getPosition();
            isPreviousBlack = false;
            isPreviousWhite = false;
            isAlreadyAte = false;
            down();
            left();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isAlreadyAte) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                down();
                left();
            }
            currentPosition = selectedChecker.getPosition();
            isPreviousBlack = false;
            isPreviousWhite = false;
            isAlreadyAte = false;
            down();
            right();
            while (insideDesk()) {
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isAlreadyAte) availablePositions.add(currentPosition);
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) isPreviousBlack = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) isPreviousWhite = true;
                if (Field.getCells().get(currentPosition).getCheckerStatus() != CheckerStatus.FREE && isAlreadyAte) return;
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousBlack && selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                if (Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE && isPreviousWhite && selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !isAlreadyAte) { isAlreadyAte = true; availablePositions.add(currentPosition); }
                down();
                right();
            }
            return;
        }

















        currentPosition = selectedChecker.getPosition();
        if (isWhiteStep) {
            down();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                down();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            down();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                down();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }

            //
            currentPosition = selectedChecker.getPosition();
            up();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                up();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            up();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.BLACK) {
                up();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
        }

        if (!isWhiteStep) {
            up();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                up();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            up();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                up();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }

            //
            currentPosition = selectedChecker.getPosition();
            down();
            left();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                down();
                left();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
            currentPosition = selectedChecker.getPosition();
            down();
            right();
            if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.WHITE) {
                down();
                right();
                if (Field.getCells().get(currentPosition) != null && Field.getCells().get(currentPosition).getCheckerStatus() == CheckerStatus.FREE)
                    availablePositions.add(currentPosition);
            }
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

    public static void checkNewSuperChecker() {
        if (Arrays.stream(superPositions).anyMatch(pos -> pos.equals(selectedChecker.getPosition()))) {
            selectedChecker.setSuperChecker(true);
        }
    }

    private static void up() {
        int y = ((int) currentPosition.charAt(0)) + 1;
        FieldModifier.currentPosition = String.valueOf((char) y) + currentPosition.charAt(1);
    }
    private static void down() {
        int y = ((int) currentPosition.charAt(0)) - 1;
        FieldModifier.currentPosition = String.valueOf((char) y) + currentPosition.charAt(1);
    }
    private static void left() {
        int x = Character.getNumericValue(currentPosition.charAt(1)) + 1;
        FieldModifier.currentPosition = currentPosition.charAt(0) + String.valueOf(x);
    }
    private static void right() {
        int x = Character.getNumericValue(currentPosition.charAt(1)) - 1;
        FieldModifier.currentPosition = currentPosition.charAt(0) + String.valueOf(x);
    }

    private static boolean insideDesk() {
        return
                Character.getNumericValue(currentPosition.charAt(1)) > 0 &&
                Character.getNumericValue(currentPosition.charAt(1)) < 9 &&
                ((int) currentPosition.charAt(0)) > 64 &&
                ((int) currentPosition.charAt(0)) < 73;
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

    public static void setAvailablePositions(List<String> availablePositions) {
        FieldModifier.availablePositions = availablePositions;
    }

    public static List<String> getAvailableSteps() {
        return FieldModifier.availablePositions;
    }

    public static boolean isWhiteStep() {
        return FieldModifier.isWhiteStep;
    }

    public static boolean isUnlockCurrentStep() {
        return FieldModifier.unlockCurrentStep;
    }

    public static void setUnlockCurrentStep(boolean lock) {
        FieldModifier.unlockCurrentStep = lock;
    }

}
