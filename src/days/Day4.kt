package days

import utils.*

class Day4 {
    val printingRoom =
        Array(numberOfLinesPerFile(4)) { CharArray(numberOfCharsPerLine(4)) }

    fun solve() {
        val rolls = mutableListOf<Point2D>()
        execFileByLineIndexed(4) { y, line ->
            line.forEachIndexed { x, v ->
                printingRoom[y][x] = v
                if (v == '@') {
                    rolls += Point2D(x, y)
                }
            }
        }

        println(rolls.count {
            it.getNeighborRolls().size < 4
        })

        var res = 0
        val currentRolls = rolls.toMutableSet()
        var eligibleRolls = currentRolls.filter { it.getNeighborRolls().size < 4 }.toMutableSet()

        while (eligibleRolls.isNotEmpty()) {
            currentRolls.removeAll(eligibleRolls)
            res += eligibleRolls.size
            eligibleRolls.forEach { printingRoom[it.y][it.x] = '.' }
            eligibleRolls = currentRolls.filter { it.getNeighborRolls().size < 4 }.toMutableSet()
        }
        println(res)
    }

    private fun Point2D.getNeighborRolls(): List<Point2D> =
        Direction.entries
            .map { this + it }
            .filter { printingRoom.atOrNull(it) == '@' }
            .map { Point2D(it.x, it.y) }
}