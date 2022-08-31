package core

class Board(boardListener: BoardListenerInterface) {

    private var listener: BoardListenerInterface


    private var currentTurn = Color.WHITE


    val listOfPositions = mutableListOf<PositionOnBoard>()

    init {
        listener = boardListener
        for (x in 0..23) {
                listOfPositions.add(PositionOnBoard())
        }
        listOfPositions[0].color = Color.WHITE
        listOfPositions[0].count = 15

        listOfPositions[12].color = Color.BLACK
        listOfPositions[12].count = 15
    }

    private fun move(from: Int, to: Int) {
        if (listOfPositions[to].color == listOfPositions[from].color || listOfPositions[to].color == Color.NEUTRAL) {

            listOfPositions[to].count += 1
            listOfPositions[to].color = listOfPositions[from].color

            listOfPositions[from].count -= 1
            if (listOfPositions[from].count == 0) {
                listOfPositions[from].color = Color.NEUTRAL
            }
        }
    }

    private fun getColorOfPosition(position: Int): Color {
        return listOfPositions[position % 24].color
    }

    private var turns: MutableList<Int> = mutableListOf()

    fun updateTurns() {
        if (turns.isEmpty()) {
            turns = Dices().rollDices()
            listener.showDices(turns[0], turns[1])
        }
    }

    fun possibleMoves(from: Int): List<Int> {
        val result = mutableListOf<Int>()
        val fromColor = getColorOfPosition(from)
// if (turn.size > 1) // else{}
        if (getColorOfPosition(from + turns.first()) == fromColor ||
            getColorOfPosition(from + turns.first()) == Color.NEUTRAL) {
            result.add(from + turns.first())
        }

        if (getColorOfPosition(from + turns[1]) == fromColor ||
            getColorOfPosition(from + turns[1]) == Color.NEUTRAL) {
            result.add(from + turns[1])
        }

        if (getColorOfPosition(from + turns.first() + turns[1]) == fromColor ||
            getColorOfPosition(from + turns.first() + turns[1]) == Color.NEUTRAL)
        {
            result.add(from + turns.first() + turns[1])
        }

        for (i in result.indices) {
            if (result[i] >= 24) {
                result[i] = result[i] % 24
            }
        }
        return result
    }

    private var canThrowBlack = false
    private var canThrowWhite = false
    fun checkPossibilityOfThrowing() {
        var allWhiteCheckersAtHome = true
        var allBlackCheckersAtHome = true
        for (i in 12..29) {
            if (getColorOfPosition(i) == Color.BLACK) allBlackCheckersAtHome = false
        }
        for (i in 0..17) {
            if (getColorOfPosition(i) == Color.WHITE) allWhiteCheckersAtHome = false
        }
        canThrowBlack = allBlackCheckersAtHome
        canThrowWhite = allWhiteCheckersAtHome
    }

    fun makeMove(from: Int, to: Int) {
        var deferenceBetweenToAndFrom = 0
        if (to - from < 0) {
            deferenceBetweenToAndFrom = to - from + 24
        } else {
            deferenceBetweenToAndFrom = to - from
        }
       /* if (positionOnBoard is Checker && chosenPosition == null) {
            chosenPosition = from
            listener!!.turnMade(mutableListOf())
            return
            }
        */
        move(from, to)
        updateTurns()
        turns.remove(deferenceBetweenToAndFrom)

        if (turns.isEmpty()) currentTurn = currentTurn.opposite()

    }

    fun clear() {
        listOfPositions.clear()
        currentTurn = Color.NEUTRAL
        for (x in 0..23) {
            listOfPositions.add(PositionOnBoard())
        }
        listOfPositions[0].color = Color.WHITE
        listOfPositions[0].count = 15

        listOfPositions[12].color = Color.BLACK
        listOfPositions[12].count = 15
    }

    fun gameOverCheck(): Color? {
        var black = false
        var white = false
        var winner: Color? = null

        for (x in 0..23) {
            if (listOfPositions[x].color == Color.BLACK) black = true
            if (listOfPositions[x].color == Color.WHITE) white = true
        }
        if (!black) winner = Color.BLACK
        if (!white) winner = Color.WHITE

        return winner
    }

    operator fun get(position: Int): PositionOnBoard = listOfPositions[position]

}