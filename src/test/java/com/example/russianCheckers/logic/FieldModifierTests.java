package com.example.russianCheckers.logic;

import com.example.russianCheckers.ui.Settings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldModifierTests {

    @Test
    public void conversionTest() {

        String pos1 = "A1";
        String pos2 = "B2";
        String pos3 = "H8";

        int x1 = FieldModifier.getXFromPosition(pos1);
        int y1 = FieldModifier.getYFromPosition(pos1);
        int x2 = FieldModifier.getXFromPosition(pos2);
        int y2 = FieldModifier.getYFromPosition(pos2);
        int x3 = FieldModifier.getXFromPosition(pos3);
        int y3 = FieldModifier.getYFromPosition(pos3);

        assertEquals(Settings.CELL_LENGTH, x1);
        assertEquals(Settings.CELL_LENGTH, y1);

        assertEquals(2 * Settings.CELL_LENGTH, x2);
        assertEquals(2 * Settings.CELL_LENGTH, y2);

        assertEquals(8 * Settings.CELL_LENGTH, x3);
        assertEquals(8 * Settings.CELL_LENGTH, y3);

        assertEquals(pos1, FieldModifier.coordinatesToPosition(x1, y1));
        assertEquals(pos2, FieldModifier.coordinatesToPosition(x2, y2));
        assertEquals(pos3, FieldModifier.coordinatesToPosition(x3, y3));
    }

    @Test
    public void findAvailableStepsTest() {
        Field.refreshField();
        Checker checker1 = Field.getCells().get("C2");
        checker1.setSuperChecker(true);
        FieldModifier.setSelectedChecker(checker1);
        FieldModifier.setIsWhiteStep(false);
        FieldModifier.findAvailableSteps();
        assertEquals(3, FieldModifier.getAvailableSteps().size());

        Checker checker2 = Field.getCells().get("A2");
        FieldModifier.setSelectedChecker(checker2);
        FieldModifier.setIsWhiteStep(false);
        FieldModifier.findAvailableSteps();
        assertEquals(0, FieldModifier.getAvailableSteps().size());


        Checker checker3 = Field.getCells().get("F3");
        FieldModifier.setSelectedChecker(checker3);
        FieldModifier.setIsWhiteStep(true);
        FieldModifier.findAvailableSteps();
        assertEquals(2, FieldModifier.getAvailableSteps().size());
    }

    @Test
    public void checkSuperCheckerTest() {
        FieldModifier.setSelectedChecker(Field.getCells().get("F3"));
        assertFalse(FieldModifier.checkNewSuperChecker());

        Checker checker = Field.getCells().get("A2");
        checker.setCheckerStatus(CheckerStatus.WHITE);
        FieldModifier.setSelectedChecker(checker);
        assertTrue(FieldModifier.checkNewSuperChecker());
    }

    @Test
    public void tryChangeSideTest() {
        FieldModifier.setSelectedChecker(Field.getCells().get("F3"));
        FieldModifier.tryChangeSide("F3", "E4");
        assertFalse(FieldModifier.isWhiteStep());

        FieldModifier.setIsWhiteStep(true);
        Field.getCells().get("E4").setCheckerStatus(CheckerStatus.BLACK);
        FieldModifier.tryChangeSide("F3", "D5");
        assertFalse(FieldModifier.isWhiteStep());
    }
}
