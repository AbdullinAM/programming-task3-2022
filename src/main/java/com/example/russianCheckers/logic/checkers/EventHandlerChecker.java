package com.example.russianCheckers.logic.checkers;

import com.example.russianCheckers.logic.CheckersLogic;
import com.example.russianCheckers.logic.fieldOptions.FieldInformation;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import static com.example.russianCheckers.logic.checkers.StatusChecker.*;

public class EventHandlerChecker {

    public static void addEvents(Checker checker) {
        EventHandler<MouseEvent> eventHandler = e -> {
            FieldInformation.getWhatEat().clear();
            if ((checker.getStatusChecker() == NOTHING_WHITE && CheckersLogic.isCurrentStep()) ||
                    (checker.getStatusChecker() == NOTHING_BLACK && !CheckersLogic.isCurrentStep())) {
                setCurrentChecker(checker);
                if (checker.isSuperChecker()) {
                    CheckersLogic.findAvailableSuperSteps(checker.getPosition(), 1, false);
                    CheckersLogic.findAvailableSuperSteps(checker.getPosition(), 2, false);
                    CheckersLogic.findAvailableSuperSteps(checker.getPosition(), 3, false);
                    CheckersLogic.findAvailableSuperSteps(checker.getPosition(), 4, false);
                } else {
                    CheckersLogic.findAvailableSteps(checker.getPosition(), 1);
                    CheckersLogic.findAvailableSteps(checker.getPosition(), 2);
                    CheckersLogic.findAvailableSteps(checker.getPosition(), 3);
                    CheckersLogic.findAvailableSteps(checker.getPosition(), 4);
                    CheckersLogic.findAvailableEatSteps(checker.getPosition(), 1);
                    CheckersLogic.findAvailableEatSteps(checker.getPosition(), 2);
                    CheckersLogic.findAvailableEatSteps(checker.getPosition(), 3);
                    CheckersLogic.findAvailableEatSteps(checker.getPosition(), 4);
                }
                CheckersLogic.printAvailableSteps();
                CheckersLogic.setToInactiveOtherCheckers(checker.getPosition(), CheckersLogic.getFriendCheckers());
                CheckersLogic.setToInactiveOtherCheckers(checker.getPosition(), CheckersLogic.getEnemyCheckers());
            }
        };
        checker.getChecker().addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    private static void setCurrentChecker(Checker checker) {
        CheckersLogic.setCurrentChecker(checker);
        if (checker.getStatusChecker() == NOTHING_WHITE && CheckersLogic.isCurrentStep()) {
            checker.updateStatus(CURRENT_CHECKER_WHITE);
        } else if (checker.getStatusChecker() == NOTHING_BLACK && !CheckersLogic.isCurrentStep()) {
            checker.updateStatus(CURRENT_CHECKER_BLACK);
        }
    }
}

