package core

interface BoardListenerInterface {
    fun turnMade(position: MutableList<PositionOnBoard>)
}