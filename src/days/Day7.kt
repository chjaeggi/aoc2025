package days

import utils.*

class Day7 {

    val manifold =
        Array(numberOfLinesPerFile(7)) { CharArray(numberOfCharsPerLine(7)) }

    var splitStart = mutableSetOf<Point2D>()
    var splitEntry = mutableSetOf<Point2D>()

    val knownTimelines = mutableMapOf<Point2D, Long>()

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
        println(nOfPaths(start!!))
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

    private fun nOfPaths(pos: Point2D, noOfTimelines: Long = 0L): Long {
        if (manifold.atOrNull(pos.s) == null) {
            return 1L
        }
        if (manifold.atOrNull(pos.s) == '.') {
            return nOfPaths(pos.s, noOfTimelines)
        }
        if (manifold.atOrNull(pos.s) == '^') {
            if (knownTimelines[pos.s] != null) {
                return knownTimelines[pos.s]!!
            }
            val res = nOfPaths(pos.sw, noOfTimelines) + nOfPaths(pos.se, noOfTimelines)
            knownTimelines[pos.s] = res
            return res
        }
        throw IllegalStateException("Should never reach this")
    }
}