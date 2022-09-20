package core

class Board(boardListener: BoardListenerInterface) {

    private var listener: BoardListenerInterface

    var turns: MutableList<Int> = mutableListOf()

    var currentTurn = Color.BLACK
    private var counterOfMovesFromHead = 0

    private var canThrowBlack = false
    private var canThrowWhite = false

    var allWhiteCheckersAtHome = true
    var allBlackCheckersAtHome = true

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

    fun updateTurns() {
        if (turns.isEmpty()) {
            currentTurn = currentTurn.opposite()
            turns = Dices().rollDices()
            listener.showDices(turns[0], turns[1])
            counterOfMovesFromHead = 0
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

                if (getColorOfPosition(from + firstTurn + secondTurn) == fromColor ||
                    getColorOfPosition(from + firstTurn + secondTurn) == Color.NEUTRAL)
                {
                    result.add(from + firstTurn + secondTurn)
                }
            }

            if (getColorOfPosition(from + secondTurn) == fromColor ||
                getColorOfPosition(from + secondTurn) == Color.NEUTRAL) {
                result.add(from + secondTurn)

                if (getColorOfPosition(from + firstTurn + secondTurn) == fromColor ||
                    getColorOfPosition(from + firstTurn + secondTurn) == Color.NEUTRAL)
                {
                    result.add(from + firstTurn + secondTurn)
                }
            }
        }
        else {
            val leftTurn = turns[0]
            if (getColorOfPosition(from + leftTurn) == fromColor ||
                getColorOfPosition(from + leftTurn) == Color.NEUTRAL) {
                result.add(from + leftTurn)
            }
        }
        if ((from == 0 && listOfPositions[from].color == Color.WHITE ||
            from == 12 && listOfPositions[from].color == Color.BLACK)
            && counterOfMovesFromHead > 0 ) {
            return emptyList()
        }

        for (i in result.indices) {
            if (result[i] >= 24) {
                result[i] = result[i] % 24
            }
        }
        //удаление ходов, которые противоречат правилам
        unableToMoveFromHome(fromColor, result, from)
        return result
    }
    //функция, чтобы шашки не ходили бесконечно по кругу
    private fun unableToMoveFromHome(fromColor: Color, listOfAddedMoves: MutableList<Int>, from: Int) {
        val copyOfListOfAddedMoves = listOfAddedMoves.toList()
        //нужна копия т.к нельзя идти по листу и одновременно что-то в нём удалять
        if (fromColor == Color.WHITE) {
            if (from in 12..23) {
               for (i in copyOfListOfAddedMoves) {
                   if (i < 12) {
                       listOfAddedMoves.remove(i)
                   }
               }
            }
        }
        if (fromColor == Color.BLACK) {
            if (from in 0..11) {
                for (i in copyOfListOfAddedMoves) {
                    if (i >= 12) {
                        listOfAddedMoves.remove(i)
                    }
                }
            }
        }
    }

    fun checkPossibilityOfThrowing() {
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
        if (turns.contains(deferenceBetweenToAndFrom)) {
            turns.remove(deferenceBetweenToAndFrom)
        } else if (deferenceBetweenToAndFrom == turns[0] + turns[1]) {
            turns.remove(turns[0])
            turns.remove(turns[0])
        }
        //счётчик снятых с головы шашек
        if (from == 0 && listOfPositions[from].color == Color.WHITE ||
            from == 12 && listOfPositions[from].color == Color.BLACK) {
            counterOfMovesFromHead += 1
        }
        move(from, to)
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