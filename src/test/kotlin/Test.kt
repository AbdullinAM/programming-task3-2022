import core.Board
import core.Color
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Test {

    @Test
    fun makeMoveTest() {
        val board = Board(FakeBoardListener())
        board.turns.clear()
        board.turns.add(3)
        board.turns.add(4)

        assertEquals(Color.NEUTRAL, board.listOfPositions[3].color)
        board.makeMove(0, 3)
        assertEquals(1, board.listOfPositions[3].count)
        assertEquals(Color.WHITE, board.listOfPositions[3].color)
        assertEquals(1, board.turns.size)
        assertEquals(14, board.listOfPositions[0].count)

        board.makeMove(3, 7)
        assertEquals(1, board.listOfPositions[7].count)
        assertEquals(0,board.listOfPositions[3].count)
        assertEquals(Color.NEUTRAL, board.listOfPositions[3].color)

        board.turns.clear()
        board.turns.add(5)
        board.turns.add(6)
        assertEquals(2, board.turns.size)
        board.makeMove(12, 17)
        assertEquals(14, board.listOfPositions[12].count)
        assertEquals(1, board.turns.size)
        board.makeMove(17, 23)
        assertEquals(Color.NEUTRAL, board.listOfPositions[17].color)
        assertEquals(Color.BLACK, board.listOfPositions[23].color)

        board.clearAllBoard()
        board.turns.clear()
        board.turns.add(2)
        board.turns.add(1)
        board.currentTurn = Color.WHITE
        board.listOfPositions[1].count = 1
        board.listOfPositions[1].color = Color.BLACK
        board.listOfPositions[2].count = 1
        board.listOfPositions[2].color = Color.BLACK
        board.listOfPositions[12].count = 13
        board.makeMove(0, 1)
        assertEquals(Color.BLACK, board.listOfPositions[1].color)
    }

    @Test
    fun possibleMovesTest() {
        val board = Board(FakeBoardListener())
        board.turns.clear()
        board.turns.add(1)
        board.turns.add(2)
        assertEquals(listOf(1,2,3).toSet(), board.possibleMoves(0).toSet())

        board.clearAllBoard()
        board.turns.clear()
        board.turns.add(6)
        board.turns.add(2)
        board.currentTurn = Color.BLACK
        board.listOfPositions[12].count = 4
        board.listOfPositions[22].count = 8
        board.listOfPositions[22].color = Color.BLACK
        board.listOfPositions[4].count = 3
        board.listOfPositions[4].color = Color.BLACK
        assertEquals(listOf(14, 18, 20).toSet(), board.possibleMoves(12).toSet())
        assertEquals(listOf(4, 6).toSet(), board.possibleMoves(22).toSet())
        assertEquals(listOf(6, 10).toSet(), board.possibleMoves(4).toSet())

        board.clearAllBoard()
        board.turns.clear()
        board.turns.add(2)
        board.turns.add(2)
        board.turns.add(2)
        board.turns.add(2)
        board.currentTurn = Color.WHITE
        assertEquals(listOf(2, 4).toSet(), board.possibleMoves(0).toSet())
        board.makeMove(0, 2)
        assertEquals(listOf(4, 6).toSet(), board.possibleMoves(2).toSet())
        board.makeMove(2, 6)
        assertEquals(listOf(8), board.possibleMoves(6))
    }

    @Test
    fun possibleToThrowTest() {
        val board = Board(FakeBoardListener())
        board.currentTurn = Color.WHITE
        board.turns.clear()
        board.turns.add(5)
        board.turns.add(6)
        board.listOfPositions[0].count = 0
        board.listOfPositions[0].color = Color.NEUTRAL
        board.listOfPositions[18].count = 14
        board.listOfPositions[18].color = Color.WHITE
        board.listOfPositions[23].count = 1
        board.listOfPositions[23].color = Color.WHITE
        board.checkPossibilityOfThrowing()
        assertTrue(board.possibleToThrow(23))
        assertTrue(board.possibleToThrow(18))

        board.clearAllBoard()
        board.turns.clear()
        board.currentTurn = Color.WHITE
        board.turns.add(1)
        board.turns.add(2)
        board.listOfPositions[0].count = 0
        board.listOfPositions[0].color = Color.NEUTRAL
        board.listOfPositions[18].count = 15
        board.listOfPositions[18].color = Color.WHITE
        board.checkPossibilityOfThrowing()
        assertFalse(board.possibleToThrow(18))

        board.clearAllBoard()
        board.turns.clear()
        board.currentTurn = Color.BLACK
        board.turns.add(4)
        board.turns.add(4)
        board.turns.add(4)
        board.turns.add(4)
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[8].count = 15
        board.listOfPositions[8].color = Color.BLACK
        board.checkPossibilityOfThrowing()
        assertTrue(board.possibleToThrow(8))

        board.clearAllBoard()
        board.turns.clear()
        board.currentTurn = Color.BLACK
        board.turns.add(3)
        board.turns.add(1)
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[7].count = 15
        board.listOfPositions[7].color = Color.BLACK
        board.checkPossibilityOfThrowing()
        assertFalse(board.possibleToThrow(7))
    }

    @Test
    fun gameOverCheckTest() {
        val board = Board(FakeBoardListener())
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[18].count = 15
        board.listOfPositions[18].color = Color.WHITE
        board.listOfPositions[0].count = 0
        board.listOfPositions[0].color = Color.NEUTRAL
        assertEquals(Color.BLACK, board.gameOverCheck())

        board.clearAllBoard()
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[0].count = 0
        board.listOfPositions[0].color = Color.NEUTRAL
        board.listOfPositions[11].count = 15
        board.listOfPositions[11].color = Color.BLACK
        assertEquals(Color.WHITE, board.gameOverCheck())

        board.clearAllBoard()
        assertEquals(null, board.gameOverCheck())
    }

    @Test
    fun clearAllBoardTest() {
        val board = Board(FakeBoardListener())
        board.listOfPositions[0].count = 10
        board.listOfPositions[5].count = 1
        board.listOfPositions[5].color = Color.WHITE
        board.listOfPositions[11].count = 4
        board.listOfPositions[11].color = Color.WHITE
        board.listOfPositions[12].count = 9
        board.listOfPositions[13].count = 1
        board.listOfPositions[13].color = Color.BLACK
        board.listOfPositions[17].count = 1
        board.listOfPositions[17].color = Color.BLACK
        board.listOfPositions[18].count = 4
        board.listOfPositions[18].color = Color.BLACK
        board.clearAllBoard()
        assertEquals(15, board.listOfPositions[0].count)
        assertEquals(Color.WHITE, board.listOfPositions[0].color)
        assertEquals(15, board.listOfPositions[12].count)
        assertEquals(Color.BLACK, board.listOfPositions[12].color)
    }

    @Test
    fun checkPossibilityOfThrowingTest() {
        val board = Board(FakeBoardListener())
        board.currentTurn = Color.WHITE
        board.listOfPositions[0].count = 0
        board.listOfPositions[0].color = Color.NEUTRAL
        board.listOfPositions[18].count = 7
        board.listOfPositions[18].color = Color.WHITE
        board.listOfPositions[20].count = 8
        board.listOfPositions[20].color = Color.WHITE
        board.checkPossibilityOfThrowing()
        assertTrue(board.canThrowWhite)

        board.clearAllBoard()
        board.currentTurn = Color.BLACK
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[10].count = 14
        board.listOfPositions[10].color = Color.BLACK
        board.listOfPositions[11].count = 1
        board.listOfPositions[11].color = Color.BLACK
        board.checkPossibilityOfThrowing()
        assertTrue(board.canThrowBlack)

        board.clearAllBoard()
        board.currentTurn = Color.WHITE
        board.listOfPositions[0].count = 0
        board.listOfPositions[0].color = Color.NEUTRAL
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[19].count = 10
        board.listOfPositions[19].color = Color.WHITE
        board.listOfPositions[23].count = 5
        board.listOfPositions[23].color = Color.WHITE
        board.checkPossibilityOfThrowing()
        assertTrue(board.canThrowWhite)
        board.currentTurn = Color.BLACK
        board.listOfPositions[9].count = 6
        board.listOfPositions[9].color = Color.BLACK
        board.listOfPositions[11].count = 4
        board.listOfPositions[11].color = Color.BLACK
        board.listOfPositions[8].count = 5
        board.listOfPositions[8].color = Color.BLACK
        board.checkPossibilityOfThrowing()
        assertTrue(board.canThrowBlack)

        board.clearAllBoard()
        board.currentTurn = Color.WHITE
        board.listOfPositions[0].count = 1
        board.listOfPositions[23].count = 14
        board.listOfPositions[23].color = Color.WHITE
        board.checkPossibilityOfThrowing()
        assertFalse(board.canThrowWhite)

        board.clearAllBoard()
        board.currentTurn = Color.BLACK
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[5].count = 1
        board.listOfPositions[5].color = Color.BLACK
        board.listOfPositions[11].count = 14
        board.listOfPositions[11].color = Color.BLACK
        board.checkPossibilityOfThrowing()
        assertFalse(board.canThrowBlack)
    }

    @Test
    fun throwOutFromTheBoardTest() {
        val board = Board(FakeBoardListener())
        board.turns.clear()
        board.turns.add(4)
        board.turns.add(5)
        board.currentTurn = Color.WHITE
        board.listOfPositions[0].count = 0
        board.listOfPositions[0].color = Color.NEUTRAL
        board.listOfPositions[23].count = 15
        board.listOfPositions[23].color = Color.WHITE
        board.throwOutFromTheBoard(23)
        assertEquals(14, board.listOfPositions[23].count)

        board.clearAllBoard()
        board.turns.clear()
        board.turns.add(6)
        board.turns.add(6)
        board.turns.add(6)
        board.turns.add(6)
        board.currentTurn = Color.BLACK
        board.listOfPositions[12].count = 0
        board.listOfPositions[12].color = Color.NEUTRAL
        board.listOfPositions[6].count = 15
        board.listOfPositions[6].color = Color.BLACK
        board.throwOutFromTheBoard(6)
        board.throwOutFromTheBoard(6)
        board.throwOutFromTheBoard(6)
        board.throwOutFromTheBoard(6)
        assertEquals(11, board.listOfPositions[6].count)
    }
}