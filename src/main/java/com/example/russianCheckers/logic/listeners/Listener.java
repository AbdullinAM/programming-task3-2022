package com.example.russianCheckers.logic.listeners;

import com.example.russianCheckers.logic.Checker;
import com.example.russianCheckers.logic.CheckerStatus;
import com.example.russianCheckers.logic.Field;
import com.example.russianCheckers.logic.FieldModifier;
import com.example.russianCheckers.ui.FieldUI;
import com.example.russianCheckers.ui.Settings;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.example.russianCheckers.logic.FieldModifier.findAvailableSteps;
import static com.example.russianCheckers.logic.FieldModifier.isNeedOnlyEat;

public class Listener {

    public static EventHandler<MouseEvent> cellListener() {
        return e -> {
            Rectangle rectangle = (Rectangle) e.getSource();
            double x = rectangle.getX() + Settings.CELL_LENGTH;
            double y = rectangle.getY() + Settings.CELL_LENGTH;
            String position = FieldModifier.coordinatesToPosition(x, y);
            if (FieldModifier.getSelectedChecker() != null) {
                if (FieldModifier.getAvailableSteps().stream().anyMatch(pos -> pos.equals(position))) {
                    Checker selectedChecker = FieldModifier.getSelectedChecker();
                    Checker currentCell = Field.getInstance().getCells().get(position);
                    Field.getInstance().getCells().put(position, selectedChecker);
                    Field.getInstance().getCells().put(selectedChecker.getPosition(), currentCell);
                    currentCell.setPosition(selectedChecker.getPosition());
                    selectedChecker.setPosition(position);
                    FieldModifier.checkNewSuperChecker();
                    FieldModifier.tryChangeSide(currentCell.getPosition(), selectedChecker.getPosition());
                    FieldModifier.tryFinalizeGame();
                }
            }
        };
    }

    public static EventHandler<MouseEvent> checkerListener() {
        return e -> {
            Circle circle = (Circle) e.getSource();
            double x = circle.getCenterX() + ((double) Settings.CELL_LENGTH) / 2;
            double y = circle.getCenterY() + ((double) Settings.CELL_LENGTH) / 2;
            String pos = FieldModifier.coordinatesToPosition(x, y);
            if (FieldModifier.isUnlockCurrentStep() && FieldModifier.findAllAvailablePositionsToStep().contains(pos)) {
                Checker selectedChecker = FieldUI
                        .getFieldUI()
                        .getCheckerUI()
                        .entrySet()
                        .stream()
                        .filter(checker -> checker.getKey().getPosition().equals(pos))
                        .findAny()
                        .get()
                        .getKey();
                if (((selectedChecker.getCheckerStatus() == CheckerStatus.WHITE && FieldModifier.isWhiteStep())
                        || (selectedChecker.getCheckerStatus() == CheckerStatus.BLACK && !FieldModifier.isWhiteStep()))) {
                    FieldModifier.setSelectedChecker(selectedChecker);
                    if (isNeedOnlyEat())
                        FieldModifier.setAvailablePositions(FieldModifier.findAvailableStepsAfterEat(selectedChecker, FieldModifier.isWhiteStep()));
                    else FieldModifier.setAvailablePositions(findAvailableSteps(selectedChecker, FieldModifier.isWhiteStep()));
                }
            }
        };
    }

}
