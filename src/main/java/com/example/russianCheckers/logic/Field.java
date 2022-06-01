package com.example.russianCheckers.logic;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Field {

    private Map<String, Checker> cells = new HashMap<>();

    private static Field instance;

    public static Field getInstance() {
        return instance;
    }
    private Field() {}

    static {
        instance = new Field();
        instance.refreshField();
    }

    public void refreshField() {
        cells.clear();
        cells.put("F1", Checker.newBuilder().setIsSuperChecker(false).setPosition("F1").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("F3", Checker.newBuilder().setIsSuperChecker(false).setPosition("F3").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("F5", Checker.newBuilder().setIsSuperChecker(false).setPosition("F5").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("F7", Checker.newBuilder().setIsSuperChecker(false).setPosition("F7").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("G2", Checker.newBuilder().setIsSuperChecker(false).setPosition("G2").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("G4", Checker.newBuilder().setIsSuperChecker(false).setPosition("G4").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("G6", Checker.newBuilder().setIsSuperChecker(false).setPosition("G6").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("G8", Checker.newBuilder().setIsSuperChecker(false).setPosition("G8").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("H1", Checker.newBuilder().setIsSuperChecker(false).setPosition("H1").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("H3", Checker.newBuilder().setIsSuperChecker(false).setPosition("H3").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("H5", Checker.newBuilder().setIsSuperChecker(false).setPosition("H5").setCheckerStatus(CheckerStatus.WHITE).build());
        cells.put("H7", Checker.newBuilder().setIsSuperChecker(false).setPosition("H7").setCheckerStatus(CheckerStatus.WHITE).build());

        cells.put("D1", Checker.newBuilder().setIsSuperChecker(false).setPosition("D1").setCheckerStatus(CheckerStatus.FREE).build());
        cells.put("D3", Checker.newBuilder().setIsSuperChecker(false).setPosition("D3").setCheckerStatus(CheckerStatus.FREE).build());
        cells.put("D5", Checker.newBuilder().setIsSuperChecker(false).setPosition("D5").setCheckerStatus(CheckerStatus.FREE).build());
        cells.put("D7", Checker.newBuilder().setIsSuperChecker(false).setPosition("D7").setCheckerStatus(CheckerStatus.FREE).build());
        cells.put("E2", Checker.newBuilder().setIsSuperChecker(false).setPosition("E2").setCheckerStatus(CheckerStatus.FREE).build());
        cells.put("E4", Checker.newBuilder().setIsSuperChecker(false).setPosition("E4").setCheckerStatus(CheckerStatus.FREE).build());
        cells.put("E6", Checker.newBuilder().setIsSuperChecker(false).setPosition("E6").setCheckerStatus(CheckerStatus.FREE).build());
        cells.put("E8", Checker.newBuilder().setIsSuperChecker(false).setPosition("E8").setCheckerStatus(CheckerStatus.FREE).build());

        cells.put("A2", Checker.newBuilder().setIsSuperChecker(false).setPosition("A2").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("A4", Checker.newBuilder().setIsSuperChecker(false).setPosition("A4").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("A6", Checker.newBuilder().setIsSuperChecker(false).setPosition("A6").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("A8", Checker.newBuilder().setIsSuperChecker(false).setPosition("A8").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("B1", Checker.newBuilder().setIsSuperChecker(false).setPosition("B1").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("B3", Checker.newBuilder().setIsSuperChecker(false).setPosition("B3").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("B5", Checker.newBuilder().setIsSuperChecker(false).setPosition("B5").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("B7", Checker.newBuilder().setIsSuperChecker(false).setPosition("B7").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("C2", Checker.newBuilder().setIsSuperChecker(false).setPosition("C2").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("C4", Checker.newBuilder().setIsSuperChecker(false).setPosition("C4").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("C6", Checker.newBuilder().setIsSuperChecker(false).setPosition("C6").setCheckerStatus(CheckerStatus.BLACK).build());
        cells.put("C8", Checker.newBuilder().setIsSuperChecker(false).setPosition("C8").setCheckerStatus(CheckerStatus.BLACK).build());
    }

    public Map<String, Checker> getCells() {
        return this.cells;
    }

}
