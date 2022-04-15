package com.example.russianCheckers.logic.checkers;

import com.example.russianCheckers.logic.CheckersLogic;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;

import static com.example.russianCheckers.logic.checkers.StatusChecker.*;
import static com.example.russianCheckers.ui.uiSettings.Settings.*;
import static javafx.scene.paint.Color.*;

@Getter
@Setter
public class Checker {

    private int centerX;
    private int centerY;
    private String position;
    private Circle checker;
    private boolean superChecker = false;
    private StatusChecker statusChecker;

    public Checker(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.position = CheckersLogic.convertCoordinateToPosition(centerX, centerY);
        Circle circle = new Circle();
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadius(35);
        circle.setStrokeWidth(5);
        this.checker = circle;
        EventHandlerChecker.addEvents(this);
    }

    public void updateStatus(StatusChecker statusChecker) {
        this.statusChecker = statusChecker;
        if (statusChecker == NOTHING_WHITE) {
            if (superChecker) {
                checker.setFill(SUPER_CHECKER_COLOR_WHITE);
            } else {
                checker.setStroke(BLACK);
                checker.setFill(WHITE_CHECKER);
            }
        }
        if (statusChecker == NOTHING_BLACK) {
            if (superChecker) {
                checker.setFill(SUPER_CHECKER_COLOR_BLACK);
            } else {
                checker.setStroke(GREY);
                checker.setFill(BLACK);
            }
        }
        if (statusChecker == CURRENT_CHECKER_WHITE) {
            checker.setStroke(BLACK);
            checker.setFill(WHITE_CHECKER_ACTIVE);
        }
        if (statusChecker == CURRENT_CHECKER_BLACK) {
            checker.setStroke(GREY);
            checker.setFill(BLACK_CHECKER_ACTIVE);
        }
    }

    public boolean isSuperChecker() {
        return this.superChecker;
    }
}
