package com.example.russianCheckers.logic.fieldOptions;

import java.util.HashMap;
import java.util.Map;
public class FieldInformation {

   private static Map<String, String> whatEat;
   private static Map<String, CellStatus> field;
   static {
      restartFieldInformation();
   }

   public static void restartFieldInformation() {
      field = new HashMap<>();
      whatEat = new HashMap<>();
      field.put("F1", CellStatus.FRIEND_CHECKER);
      field.put("F3", CellStatus.FRIEND_CHECKER);
      field.put("F5", CellStatus.FRIEND_CHECKER);
      field.put("F7", CellStatus.FRIEND_CHECKER);
      field.put("G2", CellStatus.FRIEND_CHECKER);
      field.put("G4", CellStatus.FRIEND_CHECKER);
      field.put("G6", CellStatus.FRIEND_CHECKER);
      field.put("G8", CellStatus.FRIEND_CHECKER);
      field.put("H1", CellStatus.FRIEND_CHECKER);
      field.put("H3", CellStatus.FRIEND_CHECKER);
      field.put("H5", CellStatus.FRIEND_CHECKER);
      field.put("H7", CellStatus.FRIEND_CHECKER);

      field.put("D1", CellStatus.FREE);
      field.put("D3", CellStatus.FREE);
      field.put("D5", CellStatus.FREE);
      field.put("D7", CellStatus.FREE);
      field.put("E2", CellStatus.FREE);
      field.put("E4", CellStatus.FREE);
      field.put("E6", CellStatus.FREE);
      field.put("E8", CellStatus.FREE);

      field.put("A2", CellStatus.ENEMY_CHECKER);
      field.put("A4", CellStatus.ENEMY_CHECKER);
      field.put("A6", CellStatus.ENEMY_CHECKER);
      field.put("A8", CellStatus.ENEMY_CHECKER);
      field.put("B1", CellStatus.ENEMY_CHECKER);
      field.put("B3", CellStatus.ENEMY_CHECKER);
      field.put("B5", CellStatus.ENEMY_CHECKER);
      field.put("B7", CellStatus.ENEMY_CHECKER);
      field.put("C2", CellStatus.ENEMY_CHECKER);
      field.put("C4", CellStatus.ENEMY_CHECKER);
      field.put("C6", CellStatus.ENEMY_CHECKER);
      field.put("C8", CellStatus.ENEMY_CHECKER);
   }

   public static Map<String, CellStatus> getField() {
      return field;
   }

   public static Map<String, String> getWhatEat() {
      return whatEat;
   }
}