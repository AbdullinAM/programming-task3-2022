package com.example.checkers.controller.entity;

import com.example.checkers.view.entity.FieldState;

public class Field {
    private FieldState color;

    private Checker checker;

    public Field() {
        this.color = FieldState.EMPTY;
    }

    public FieldState getState() {
        return color;
    }

    public void setState(FieldState color) {
        this.color = color;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public Checker getChecker() {
        return checker;
    }

    public void deleteChecker() {
        this.checker = null;
    }

}
