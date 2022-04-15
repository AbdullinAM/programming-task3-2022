package com.example.russianCheckers.logic.fieldOptions;

import com.example.russianCheckers.logic.CheckersLogic;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerCell {

    public static void addEvent(CellField cellField) {
        EventHandler<MouseEvent> eventHandler = e -> {
            if (e.getButton() == MouseButton.PRIMARY && cellField.getCellStatus() == CellStatus.ACTIVE) {
                if (cellField.getPosition().equals("A2") ||
                        cellField.getPosition().equals("A4") ||
                        cellField.getPosition().equals("A6") ||
                        cellField.getPosition().equals("A8") ||
                        cellField.getPosition().equals("H1") ||
                        cellField.getPosition().equals("H3") ||
                        cellField.getPosition().equals("H5") ||
                        cellField.getPosition().equals("H7")
                ) {
                    CheckersLogic.getCurrentChecker().setSuperChecker(true);
                }
                CheckersLogic.eatCheckerIfPossible(cellField.getPosition());
                CheckersLogic.translateChecker(cellField.getPosition());
                CheckersLogic.changeSideIfPossibleOrRestart(cellField.getPosition());
            }
        };
        cellField.getCell().addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }
}
