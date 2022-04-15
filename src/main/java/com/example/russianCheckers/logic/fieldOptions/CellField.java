package com.example.russianCheckers.logic.fieldOptions;

import com.example.russianCheckers.logic.CheckersLogic;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import static com.example.russianCheckers.logic.fieldOptions.CellStatus.ACTIVE;
import static com.example.russianCheckers.logic.fieldOptions.CellStatus.INACTIVE;
import static com.example.russianCheckers.ui.uiSettings.Settings.*;

@Getter
@Setter
public class CellField {

    private String position;
    private Color color;
    private Rectangle cell;
    private CellStatus cellStatus;
    private int x;
    private int y;

    public CellField(int x, int y) {
        this.position = CheckersLogic.convertCoordinateToPosition(x, y);
        this.x = x;
        this.y = y;
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(CELL_LENGTH);
        rectangle.setHeight(CELL_LENGTH);
        this.cell = rectangle;
        EventHandlerCell.addEvent(this);
    }

    public void updateCell(CellStatus cellStatus) {
        this.cellStatus = cellStatus;
        if (cellStatus == INACTIVE) {
            cell.setFill(FIELD_COLOR_TWO);
        }
        if (cellStatus == ACTIVE) {
            cell.setFill(ACTIVE_COLOR_CELL);
        }
    }
}
