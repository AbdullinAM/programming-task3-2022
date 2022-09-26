package com.example.checkers;

import com.example.checkers.controller.entity.Checker;
import com.example.checkers.controller.entity.Field;
import com.example.checkers.controller.entity.GridState;
import org.junit.jupiter.api.*;
import com.example.checkers.view.entity.FieldState;
import com.example.checkers.controller.entity.Field;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CheckersTest {
    @Test
    void checkColor() {
        GridState state = new GridState();
        assertFalse(state.getFieldButtons()[0][0].getChecker().isWhiteColour());
        assertTrue(state.getFieldButtons()[0][6].getChecker().isWhiteColour());
    }

    @Test
    void checkAttackWhiteTurn() {
        GridState state = new GridState();
        ArrayList<Field> attackFields = new ArrayList<>();
        state.getFieldButtons()[2][4].setChecker(new Checker(Checker.Colour.BLACK));
        state.getFieldButtons()[4][4].setChecker(new Checker(Checker.Colour.BLACK));
        state.getFieldButtons()[5][3].setChecker(new Checker(Checker.Colour.WHITE));
        attackFields = state.attackFields(true);
        for (Field field : attackFields) field.setState(FieldState.ATTACK);
        assertSame(state.getFieldButtons()[2][4].getState(), FieldState.ATTACK);
        assertSame(state.getFieldButtons()[4][4].getState(), FieldState.ATTACK);
        assertNotSame(state.getFieldButtons()[5][3].getState(), FieldState.ATTACK);
    }

    @Test
    void checkAttackBlackTurn() {
        GridState state = new GridState();
        ArrayList<Field> attackFields = new ArrayList<>();
        state.getFieldButtons()[2][4].setChecker(new Checker(Checker.Colour.BLACK));
        state.getFieldButtons()[4][4].setChecker(new Checker(Checker.Colour.BLACK));
        state.getFieldButtons()[5][3].setChecker(new Checker(Checker.Colour.WHITE));
        attackFields = state.attackFields(false);
        for (Field field : attackFields) field.setState(FieldState.ATTACK);
        assertNotSame(state.getFieldButtons()[2][4].getState(), FieldState.ATTACK);
        assertNotSame(state.getFieldButtons()[4][4].getState(), FieldState.ATTACK);
        assertSame(state.getFieldButtons()[5][3].getState(), FieldState.ATTACK);
    }
}
