package days

import utils.*

class Day7 {

    val manifold = Array(numberOfLinesPerFile(7)) { CharArray(numberOfCharsPerLine(7)) }
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
        println(runBeam(start!!))
        println(nOfPaths(start!!))
    }

    private fun runBeam(
        pos: Point2D,
        splitPoints: MutableSet<Point2D> = mutableSetOf(),
        visited: MutableSet<Point2D> = mutableSetOf(),
    ): Int {
        if (manifold.atOrNull(pos.s) == '.') {
            runBeam(pos.s, splitPoints, visited)
        }
        if (manifold.atOrNull(pos.s) == '^') {
            splitPoints += pos.s

            for (d in listOf(pos.sw, pos.se)) {
                manifold.atOrNull(d)?.let {
                    if (d !in visited) {
                        visited += d
                        runBeam(d, splitPoints, visited)
                    }
                }
            }
        }
        return splitPoints.size
    }

    private fun nOfPaths(pos: Point2D, cache: MutableMap<Point2D, Long> = mutableMapOf()): Long {
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