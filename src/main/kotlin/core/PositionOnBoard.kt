package core

class PositionOnBoard(var count: Int = 0, var color: Color = Color.NEUTRAL) {

    fun isNotEmpty(): Boolean {
        return count > 0
    }
}