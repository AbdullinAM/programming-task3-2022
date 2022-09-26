package com.example.checkers.controller.entity;

public class Checker {

    private final Colour colour;

    private boolean Royal = false;

    public Checker(Colour colour) {
        this.colour = colour;
    }

    public enum Colour {
        WHITE, BLACK;
    }

    public void setRoyal() {
        this.Royal = true;
    }

    public boolean isRoyal() {
        return Royal;
    }

    public boolean isWhiteColour() {
        if (this.colour == Colour.WHITE) return true;
        else return false;
    }
}

