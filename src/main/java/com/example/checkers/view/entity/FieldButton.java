package com.example.checkers.view.entity;

import javafx.scene.control.Button;
import javafx.util.Pair;

public class FieldButton extends Button {

    private Pair<Integer, Integer> point;

    public FieldButton(Pair<Integer, Integer> point) {
        this.point = point;
    }

    public Pair<Integer, Integer> getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return point.toString();
    }
}

