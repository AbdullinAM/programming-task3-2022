package core

class Queen(color: Color): Checker(color) {
    override fun possibleTurns(cell: Cell): Pair<Boolean, List<Cell>> {
        val checkers = getCheckers()
        val result = mutableListOf<Cell>()
        val canEat = mutableListOf<Cell>()
        for (direction in DIRECTIONS) {
            var newCell = cell + direction
            while (checkers[newCell] == null && correct(newCell)) {
                result.add(newCell)
                newCell += direction
            }
        }
        for (direction in DIRECTIONS) {
            var newCell = cell + direction
            while (correct(newCell)) {
                if (checkers[newCell] != null && !isOpposite(checkers[newCell]))
                    break
                if (isOpposite(checkers[newCell])) {
                    var nextCell = newCell + direction
                    while (correct(nextCell) && checkers[nextCell] == null) {
                        canEat.add(nextCell)
                        nextCell += direction
                    }
                    break
                }
                newCell += direction
            }
        }
        return if (canEat.isNotEmpty()) Pair(true, canEat.toSet().toList()) else Pair(false, result)
    }
}