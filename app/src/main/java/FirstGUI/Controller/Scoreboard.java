package FirstGUI.Controller;

import FirstGUI.Model.ChipColor;

/*Стараюсь в "программирование на уровне интерфейсов"*/
public interface Scoreboard {

    String getName(ChipColor winner);

    void winnerIs(ChipColor Color);

    void changeColors();
}
