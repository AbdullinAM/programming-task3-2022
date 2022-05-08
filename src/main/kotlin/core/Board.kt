package core

open class Board (private val width: Int = 8, private val height: Int = 8) {
    var turn = Color.WHITE

    private var chooseCell: Cell? = null

    private val checkers: MutableMap<Cell, Checker?> = mutableMapOf()

    fun getAllCheckers() = checkers

    private var listener: BoardListener? = null

    private var comboEat = false

    init {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val cell = Cell(x, y)
                when {
                    (y <= 2) && ((y + x) % 2 == 1) -> {
                        val checker = Checker(Color.BLACK).also {
                            it.setMainBoard(this)
                        }
                        checkers[cell] = checker
                    }
                    (y >= 5) && (x + y) % 2 == 1 -> {
                        val checker = Checker(Color.WHITE).also {
                            it.setMainBoard(this)
                        }
                        checkers[cell] = checker
                    }
                }
            }
        }
    }

    operator fun get(x: Int, y: Int): Checker? = get(Cell(x, y))

    operator fun get(cell: Cell): Checker? = checkers[cell]

    fun possibleTurns(cell: Cell): Pair<Boolean, List<Cell>> {
        return if (checkers[cell] == null) Pair(false, emptyList())
        else checkers[cell]!!.possibleTurns(cell)
    }

    private fun playerShouldEat() : List<Cell> {
        val result = mutableListOf<Cell>()
        for ((cell, checker) in checkers)
            if (checker!!.color == turn)
                if (possibleTurns(cell).first) result.add(cell)
        return result
    }

    fun makeTurn(cell: Cell) {
        if (checkers[cell] is Checker && !comboEat && turn == checkers[cell]!!.color) {
            chooseCell = cell
            return
        }
        val shouldEat = playerShouldEat()
        if (chooseCell != null && (shouldEat.isEmpty() || shouldEat.contains(chooseCell))) {
            val mustEat = possibleTurns(chooseCell!!).first
            val possibleMoves = possibleTurns(chooseCell!!).second
            if (possibleMoves.contains(cell)) {
                val cells = mutableListOf<Cell>()
                if ((turn == Color.WHITE && cell.y == 0) || (turn == Color.BLACK && cell.y == 7)) {
                    val queen = Queen(turn)
                    queen.setMainBoard(this)
                    checkers[cell] = queen
                } else checkers[cell] = checkers[chooseCell]
                checkers.remove(chooseCell)
                cells.add(chooseCell!!)
                cells.add(cell)
                if (mustEat) {
                    var eatenCheck: Cell? = null
                    var nextCell = chooseCell!! + Cell(1, 1)
                    while (cell.x > nextCell.x && cell.y > nextCell.y && nextCell.y in 0..7 && nextCell.x in 0..7) {
                        if (eatenCheck != null) break
                        if (turn.opposite() == checkers[nextCell]?.color && checkers[nextCell] != null)
                            eatenCheck = nextCell
                        nextCell += Cell(1, 1)
                    }
                    nextCell = chooseCell!! + Cell(-1, 1)
                    while (cell.x < nextCell.x && cell.y > nextCell.y && nextCell.y in 0..7 && nextCell.x in 0..7) {
                        if (eatenCheck != null) break
                        if (turn.opposite() == checkers[nextCell]?.color && checkers[nextCell] != null)
                            eatenCheck = nextCell
                        nextCell += Cell(-1, 1)
                    }
                    nextCell = chooseCell!! + Cell(-1, -1)
                    while (cell.x < nextCell.x && cell.y < nextCell.y && nextCell.y in 0..7 && nextCell.x in 0..7) {
                        if (eatenCheck != null) break
                        if (turn.opposite() == checkers[nextCell]?.color && checkers[nextCell] != null)
                            eatenCheck = nextCell
                        nextCell += Cell(-1, -1)
                    }
                    nextCell = chooseCell!! + Cell(1, -1)
                    while (cell.x > nextCell.x && cell.y < nextCell.y && nextCell.y in 0..7 && nextCell.x in 0..7) {
                        if (eatenCheck != null) break
                        if (turn.opposite() == checkers[nextCell]?.color && checkers[nextCell] != null)
                            eatenCheck = nextCell
                        nextCell += Cell(1, -1)
                    }
                    checkers.remove(eatenCheck)
                    cells.add(eatenCheck!!)
                    if (possibleTurns(cell).first) {
                        chooseCell = cell
                        comboEat = true
                        listener!!.turnMade(cells)
                        return
                    }
                }
                chooseCell = null
                turn = turn.opposite()
                comboEat = false
                listener!!.turnMade(cells)
            }
        }
    }

    fun clear() {
        checkers.clear()
        turn = Color.WHITE
        chooseCell = null
        for (x in 0 until width) {
            for (y in 0 until height) {
                val cell = Cell(x, y)
                when {
                    (y <= 2) && ((y + x) % 2 == 1) -> {
                        val checker = Checker(Color.BLACK)
                        checker.setMainBoard(this)
                        checkers[cell] = checker
                    }
                    (y >= 5) && (x + y) % 2 == 1 -> {
                        val checker = Checker(Color.WHITE)
                        checker.setMainBoard(this)
                        checkers[cell] = checker
                    }
                }
            }
        }
    }

    fun registerListener(listener: BoardListener) {
        this.listener = listener
    }

    fun gameOver(): Color? {
        var checkBlack = false
        var checkWhite = false
        for (value in checkers.values) {
            if (value!!.color == Color.WHITE) checkWhite = true
            if (value.color == Color.BLACK) checkBlack = true
        }
        if (checkBlack && !checkWhite) return Color.BLACK
        if (checkWhite && !checkBlack) return Color.WHITE
        return null
    }

    override fun toString(): String = if (this.turn == Color.WHITE) "white_checker" else "black_checker"
}