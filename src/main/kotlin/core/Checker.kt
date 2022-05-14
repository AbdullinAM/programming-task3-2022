package core

open class Checker(val color: Color) {

    companion object {
        val DIRECTIONS = arrayOf(Cell(1,-1), Cell(-1,-1), Cell(1,1), Cell(-1,1))
    }

    private var mainBoard: Board? = null

    fun setMainBoard(board: Board) {
        this.mainBoard = board
    }

    fun isOpposite(other: Checker?) = other != null && other.color != this.color

    fun correct(cell: Cell): Boolean {
        return cell.x in 0 until 8 && cell.y in 0 until 8
    }

    fun getCheckers() = mainBoard!!.getAllCheckers()

    open fun possibleTurns(cell : Cell): Pair<Boolean, List<Cell>> {
        val checkers = getCheckers()
        val result = mutableListOf<Cell>()
        val canEat = mutableListOf<Cell>()
        if (color == Color.WHITE) {
            for (i in 0..1) {
                val newCell = cell + DIRECTIONS[i]
                if (correct(newCell)) {
                    if (checkers[newCell] == null) result.add(newCell)
                }
            }
        }
        if (color == Color.BLACK) {
            for (i in 2..3) {
                val newCell = cell + DIRECTIONS[i]
                if (correct(newCell)) {
                    if (checkers[newCell] == null) result.add(newCell)
                }
            }
        }
        for (i in 0..3) {
            val newCell = cell + DIRECTIONS[i]
            if (correct(newCell)) {
                val nextCell = newCell + DIRECTIONS[i]
                if (isOpposite(checkers[newCell]) && correct(nextCell) && checkers[nextCell] == null)
                    canEat.add(nextCell)
            }
        }
        return if (canEat.isEmpty()) Pair(false , result) else Pair(true, canEat)
    }
}