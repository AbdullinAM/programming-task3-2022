package core

import org.junit.Test
import kotlin.test.assertEquals

class Test {

    @Test
    fun abvgd() {
        val board = Board()
        assertEquals(listOf<Int>(12,12), board.possibleMoves(22))
    }
}