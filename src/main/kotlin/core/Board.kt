package core

class Board(boardListener: BoardListenerInterface) {

    private var listener: BoardListenerInterface


    var currentTurn = Color.WHITE


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
            currentTurn = currentTurn.opposite()
            turns = Dices().rollDices()
            listener.showDices(turns[0], turns[1])
        }
    }

    fun possibleMoves(from: Int): List<Int> {
        val result = mutableListOf<Int>()
        val fromColor = getColorOfPosition(from)
        val firstTurn: Int
        val secondTurn: Int
        if (turns.size > 1) {
            firstTurn = turns[0]
            secondTurn = turns[1]
            if (getColorOfPosition(from + firstTurn) == fromColor ||
                getColorOfPosition(from + firstTurn) == Color.NEUTRAL) {
                result.add(from + firstTurn)
            }

            if (getColorOfPosition(from + secondTurn) == fromColor ||
                getColorOfPosition(from + secondTurn) == Color.NEUTRAL) {
                result.add(from + secondTurn)
            }
            if (getColorOfPosition(from + firstTurn + secondTurn) == fromColor ||
                getColorOfPosition(from + firstTurn + secondTurn) == Color.NEUTRAL)
          {
                result.add(from + firstTurn + secondTurn)
          }
        }
        else {
            val leftTurn = turns[0]
            if (getColorOfPosition(from + leftTurn) == fromColor ||
                getColorOfPosition(from + leftTurn) == Color.NEUTRAL) {
                result.add(from + leftTurn)
            }
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
        val deferenceBetweenToAndFrom: Int
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
        turns.remove(deferenceBetweenToAndFrom)
        updateTurns()
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