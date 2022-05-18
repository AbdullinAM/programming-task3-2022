package FirstGUI;
import FirstGUI.Controller.Cntrllr;
import FirstGUI.Model.*;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    /*getPossibleTurns - major and most used method, let's check it's working properly*/

    /*Check that first turn rules are followed*/
    @Test
    void checkFirstTurnRules () {
        /*Init the field*/
        JFXPanel jfxPanel = new JFXPanel();
        Model model = new Model();
        model.listener = new EmptyModelListener();
        Field field = model.getField();
        /*If you made one turn from base, but you can't move this chip further, you can make second turn from the base.*/
        model.setTurnsLeft(new ArrayList<>(Arrays.asList(6,6,6,6)));
        model.makeTurn(0,6);
        List<Integer> turns = model.getPossibleTurns(0);
        assertFalse(turns.isEmpty());
        /*But you can't take the third chip*/
        model.makeTurn(0,6);
        turns = model.getPossibleTurns(0);
        assertTrue(turns.isEmpty());
        /*Then let's test second turn (regular situation) => you can make only one turn from the base*/
        model.passTheTurn();
        model.passTheTurn();
        model.makeTurn(0,6);
        turns = model.getPossibleTurns(0);
        assertTrue(turns.isEmpty());
    }

    /*Check exits properly working*/
    @Test
    void CantGoThroughClosedExit() {
        /*Init and Clear the field*/
        JFXPanel jfxPanel = new JFXPanel();
        Model model = new Model();
        Field field = model.getField();
        field.set(0, new GroupOfChips(0, null));
        field.set(12, new GroupOfChips(0, null));
        /*Put some chips in the last quarter except one chip => chips can't go out of field*/
        field.set(23, new GroupOfChips(3, ChipColor.WHITE));
        field.set(10, new GroupOfChips(1, ChipColor.WHITE));
        model.rollNewTurns();
        model.openExitsIfPossible();
        List<Integer> turns = model.getPossibleTurns(23);
        assertTrue( turns.isEmpty());
        /*Removing chip which not in last quarter => chips from last quarter can exit the field*/
        field.set(10, new GroupOfChips(0,null));
        model.openExitsIfPossible();
        turns = model.getPossibleTurns(23);
        assertFalse(turns.isEmpty());;
    }

//    @Test
//    void () {
//    }

//    @Test
//    void () {
//    }





}
