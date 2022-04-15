package example.russianCheckers.logic;

import com.example.russianCheckers.logic.CheckersLogic;
import com.example.russianCheckers.logic.Field;
import com.example.russianCheckers.logic.checkers.Checker;
import javafx.scene.Group;
import org.junit.jupiter.api.Test;

import static com.example.russianCheckers.logic.checkers.StatusChecker.CURRENT_CHECKER_WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckersLogicTest {

    private Field field;
    // init Game context
    private CheckersLogicTest() {
        this.field = Field.getInstance();
        CheckersLogic.restartGame();
        CheckersLogic.setField(field);
        field.setGroup(new Group());
        field.setFriendCheckers(CheckersLogic.getFriendCheckers());
        field.setEnemyCheckers(CheckersLogic.getEnemyCheckers());
        field.printInitCheckers();
    }

    @Test
    public void translation() {

        assertEquals(550, CheckersLogic.convertPositionToCoordinateX("H6"));
        assertEquals(750, CheckersLogic.convertPositionToCoordinateY("H6"));
        Checker checker = new Checker(550, 750);
        CheckersLogic.setCurrentChecker(checker);
        CheckersLogic.translateChecker("E5");
        assertEquals("E5", checker.getPosition());
        assertEquals("H6", CheckersLogic.convertCoordinateToPosition(550, 750));
    }

    @Test
    public void findAvailableSteps() {

        CheckersLogic.findAvailableSteps("A1", 1);
        CheckersLogic.findAvailableSteps("A1", 2);
        CheckersLogic.findAvailableSteps("A1", 3);
        CheckersLogic.findAvailableSteps("A1", 4);
        assertEquals(0, CheckersLogic.getAvailablePositions().size());
        CheckersLogic.getAvailablePositions().clear();

        CheckersLogic.findAvailableSteps("F3", 1);
        CheckersLogic.findAvailableSteps("F3", 2);
        CheckersLogic.findAvailableSteps("F3", 3);
        CheckersLogic.findAvailableSteps("F3", 4);

        assertEquals(2, CheckersLogic.getAvailablePositions().size());
        CheckersLogic.getAvailablePositions().clear();

        CheckersLogic.findAvailableSteps("F5", 1);
        CheckersLogic.findAvailableSteps("F5", 2);
        CheckersLogic.findAvailableSteps("F5", 3);
        CheckersLogic.findAvailableSteps("F5", 4);

        assertEquals(2, CheckersLogic.getAvailablePositions().size());
        CheckersLogic.getAvailablePositions().clear();
    }

    @Test
    public void eatCheckers() {

        Checker checker = new Checker(150, 150);
        CheckersLogic.setCurrentChecker(checker);
        CheckersLogic.getCurrentChecker().setStatusChecker(CURRENT_CHECKER_WHITE);
        CheckersLogic.eatAllCheckersInLine("A2", "G8");

        assertEquals(21,CheckersLogic.getFriendCheckers().size() + CheckersLogic.getEnemyCheckers().size());
    }

}
