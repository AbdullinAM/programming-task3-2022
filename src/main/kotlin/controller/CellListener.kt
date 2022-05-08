package controller

import core.Cell

interface CellListener {
    fun cellClicked(cell: Cell)
}