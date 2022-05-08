package core


interface BoardListener {
    fun turnMade(cell: MutableList<Cell>)
}