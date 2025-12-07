package days

import utils.*

class Day7 {

    val manifold = Array(numberOfLinesPerFile(7)) { CharArray(numberOfCharsPerLine(7)) }

    var splitStart = mutableSetOf<Point2D>()
    var splitEntry = mutableSetOf<Point2D>()

    var start: Point2D? = null
    fun solve() {
        execFileByLineIndexed(7) { y, line ->
            line.forEachIndexed { x, v ->
                manifold[y][x] = v
                if (v == 'S') {
                    start = Point2D(x, y)
                }
            }
        }
        runBeam(start!!)
        println(splitEntry.size)
        println(nOfPaths(start!!, mutableMapOf()))
    }

    private fun runBeam(pos: Point2D) {
        if (manifold.atOrNull(pos.s) == '.') {
            runBeam(pos.s)
        }
        if (manifold.atOrNull(pos.s) == '^') {
            splitEntry += pos.s

            for (d in listOf(pos.sw, pos.se)) {
                manifold.atOrNull(d)?.let {
                    if (d !in splitStart) {
                        splitStart += d
                        runBeam(d)
                    }
                }
            }
        }
    }

    private fun nOfPaths(pos: Point2D, cache: MutableMap<Point2D, Long>): Long {
        return cache.getOrPut(pos.s) {
            when (manifold.atOrNull(pos.s)) {
                null -> 1L
                '.' -> nOfPaths(pos.s, cache)
                '^' -> nOfPaths(pos.sw, cache) + nOfPaths(pos.se, cache)
                else -> throw IllegalStateException("Should never reach this")
            }
        }
    }
}