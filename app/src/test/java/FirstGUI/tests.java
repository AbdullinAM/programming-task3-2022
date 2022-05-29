package FirstGUI;
import FirstGUI.Model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class tests {

    @Test
    void checkBasics() {
        Model model = new Model().setModelListener(new EmptyModelListener());
        Field field = model.getField();
        /* Делаем два обычных хода с головы и проверяем что фишки убавились и появились где надо */
        model.setTurnsLeft(new ArrayList<>(List.of(5,6)));
        model.makeTurn(0,5);
        model.makeTurn(0,6);
        assertTrue(model.isNoTurnsLeft());
        assertEquals(13, field.get(0).getQuantity());
        assertEquals(1, field.get(5).getQuantity());
        assertEquals(1, field.get(6).getQuantity());
        assertEquals(ChipColor.WHITE, field.get(5).getColor());
        assertEquals(ChipColor.WHITE, field.get(6).getColor());
        assertTrue(model.isNoTurnsLeft());
        /* Делаем ход не с головы и всё проверяем*/
        model.setTurnsLeft(new ArrayList<>(List.of(3)));
        model.makeTurn(5,8);
        assertEquals(0, field.get(5).getQuantity());
        assertEquals(null, field.get(5).getColor());
        assertEquals(1, field.get(8).getQuantity());
        assertEquals(ChipColor.WHITE, field.get(8).getColor());
        assertTrue(model.isNoTurnsLeft());
        /* Проверяем что нельзя походить на фишку оппонента */
        assertThrowsExactly(IllegalArgumentException.class, () -> model.makeTurn(6,12));
        /* Проверяем что нельзя сделать ход из колонки в которой нет фишек */
        assertThrowsExactly(IllegalArgumentException.class, () -> model.makeTurn(2,3));
        /* Теперь для чёрных, делаем два хода с головы*/
        model.setTurnsLeft(new ArrayList<>(List.of(5,6)));
        model.makeTurn(12,17);
        model.makeTurn(12,18);
        assertTrue(model.isNoTurnsLeft());
        assertEquals(13, field.get(12).getQuantity());
        assertEquals(1, field.get(17).getQuantity());
        assertEquals(1, field.get(18).getQuantity());
        assertEquals(ChipColor.BLACK, field.get(17).getColor());
        assertEquals(ChipColor.BLACK, field.get(18).getColor());
        assertTrue(model.isNoTurnsLeft());
        /* Делаем ход не с головы и всё проверяем*/
        model.setTurnsLeft(new ArrayList<>(List.of(3)));
        model.makeTurn(17,20);
        assertEquals(0, field.get(17).getQuantity());
        assertEquals(null, field.get(17).getColor());
        assertEquals(1, field.get(20).getQuantity());
        assertEquals(ChipColor.BLACK, field.get(20).getColor());
        assertTrue(model.isNoTurnsLeft());
        /* Ещё проверим что нормально делаются ходы когда приходится
         * перескакивать в начало списка (например 20 -> 0).*/
        model.setTurnsLeft(new ArrayList<>(List.of(4)));
        field.set(0, new GroupOfChips());
        model.makeTurn(20, 0);
        assertEquals(0, field.get(20).getQuantity());
        assertEquals(null, field.get(20).getColor());
        assertEquals(1, field.get(0).getQuantity());
        assertEquals(ChipColor.BLACK, field.get(0).getColor());
        assertTrue(model.isNoTurnsLeft());
    }

    /*getPossibleTurns - major and most used method, let's check that it's following all game rules */

    /*Check that first turn rules are followed*/
    @Test
    void checkFirstTurnRules () {
        /*Init the field*/
        Model model = new Model().setModelListener(new EmptyModelListener());
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
        Model model = new Model().setModelListener(new EmptyModelListener());
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
        assertFalse(turns.isEmpty());
    }


    @Test
    void checkBlockRules() {
        Model model = new Model().setModelListener(new EmptyModelListener());
        Field field = model.getField();
        /* Сначала тривиальные ситуации для обоих цветов */
        /* Пока черные не сделали ни одного хода, белые не могут поставить блок нигде*/
        model.makeTurn(0, 1);
        model.makeTurn(0, 2);
        model.makeTurn(0, 3);
        model.makeTurn(0, 4);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertTrue(model.getPossibleTurns(0).isEmpty());
        model.makeTurn(0, 6);
        model.makeTurn(1, 7);
        model.makeTurn(2, 8);
        model.makeTurn(3, 9);
        model.makeTurn(4, 10);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertTrue(model.getPossibleTurns(0).isEmpty());
        model.makeTurn(6, 14);
        model.makeTurn(7, 15);
        model.makeTurn(8, 16);
        model.makeTurn(9, 17);
        model.makeTurn(10, 18);
        model.makeTurn(0, 8);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertTrue(model.getPossibleTurns(8).isEmpty());
        /*Теперь делаем ход чёрными и убеждаемся что можем поставить блок белыми там где черные уже прошли*/
        model.makeTurn(12, 19);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertFalse(model.getPossibleTurns(8).isEmpty());

        /*Теперь то же самое для чёрных*/
        field.clear();
        model.makeTurn(12, 13);
        model.makeTurn(12, 14);
        model.makeTurn(12, 15);
        model.makeTurn(12, 16);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertTrue(model.getPossibleTurns(12).isEmpty());
        model.makeTurn(12, 18);
        model.makeTurn(13, 19);
        model.makeTurn(14, 20);
        model.makeTurn(15, 21);
        model.makeTurn(16, 22);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertTrue(model.getPossibleTurns(12).isEmpty());
        model.makeTurn(18, 2);
        model.makeTurn(19, 3);
        model.makeTurn(20, 4);
        model.makeTurn(21, 5);
        model.makeTurn(22, 6);
        model.makeTurn(12, 20);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertTrue(model.getPossibleTurns(20).isEmpty());
        model.makeTurn(0, 7);
        model.setTurnsLeft(new ArrayList<>(List.of(5)));
        assertFalse(model.getPossibleTurns(20).isEmpty());

        /* Теперь моделируем ситуацию когда можно ставить 6 подряд, и это не будет считаться блоком потому что
         *  будет находиться на стыке начала и конца пути для противоположной команды.
         *  (Например, ставим блок белыми так, чтобы частью он приходился на конец пути черных, а частью на начало) */
        model.restart();
        field.set(9, new GroupOfChips(1,ChipColor.WHITE));
        field.set(10, new GroupOfChips(1,ChipColor.WHITE));
        field.set(11, new GroupOfChips(1,ChipColor.WHITE));
        field.set(12, new GroupOfChips());
        field.set(13, new GroupOfChips(1,ChipColor.WHITE));
        field.set(14, new GroupOfChips(1,ChipColor.WHITE));
        model.setTurnsLeft(new ArrayList<>(List.of(12)));
        assertFalse(model.getPossibleTurns(0).isEmpty());
        model.makeTurn(11,12);
        model.setTurnsLeft(new ArrayList<>(List.of(11)));
        assertFalse(model.getPossibleTurns(0).isEmpty());
        /*Теперь для чёрных*/
        model.restart();
        field.set(21, new GroupOfChips(1,ChipColor.BLACK));
        field.set(22, new GroupOfChips(1,ChipColor.BLACK));
        field.set(23, new GroupOfChips(1,ChipColor.BLACK));
        field.set(0, new GroupOfChips());
        field.set(1, new GroupOfChips(1,ChipColor.BLACK));
        field.set(2, new GroupOfChips(1,ChipColor.BLACK));
        model.setTurnsLeft(new ArrayList<>(List.of(12)));
        assertFalse(model.getPossibleTurns(12).isEmpty());
        model.makeTurn(23,0);
        model.setTurnsLeft(new ArrayList<>(List.of(11)));
        assertFalse(model.getPossibleTurns(12).isEmpty());
    }

    @Test
    void quantityDecreasesWhenChipExitsField() {
        Model model = new Model().setModelListener(new EmptyModelListener());
        Field field = model.getField();
        field.set(20, new GroupOfChips(15, ChipColor.WHITE));
        field.set(10, new GroupOfChips(15,ChipColor.BLACK));
        model.makeTurn(10, 24);
        model.makeTurn(20, 24);
        assertEquals(14, field.get(10).getQuantity());
        assertEquals(14, field.get(20).getQuantity());
        /* (makeTurn не должна следить за тем открыты ли уже выходы с поля,
         * за этим следит getPossibleTurns и тесты это уже покрывают)*/
    }




}
